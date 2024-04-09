package dev.zephyr.protocol.world

import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import com.comphenix.protocol.wrappers.WrappedBlockData.createData
import dev.zephyr.event.EventContext
import dev.zephyr.event.filter
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.createWrappedBlockData
import dev.zephyr.protocol.packet.world.PacketBlockChange
import dev.zephyr.protocol.world.batch.BlockBatcher
import dev.zephyr.protocol.world.batch.DirectPacketBlockBatcher
import dev.zephyr.protocol.world.block.ProtocolBlock
import dev.zephyr.protocol.world.event.block.ProtocolBlockEvent
import dev.zephyr.protocol.wrap
import dev.zephyr.util.block.AirBlockData
import dev.zephyr.util.bukkit.isChunkLoaded
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.concurrent.threadLocal
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.map
import dev.zephyr.util.shape.Shape
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

private typealias ChunkBlockMap = MutableMap<Position, Array<WrappedBlockData?>>

@KotlinOpens
class ProtocolStructure(val world: World) : ProtocolObject() {

    protected val contexts = threadLocal { ProtocolStructureBatch() }

    var batcher: BlockBatcher = DirectPacketBlockBatcher
    var removeByReplace = true

    val chunkMap: ChunkBlockMap = concurrentHashMapOf<Position, Array<WrappedBlockData?>>()

    val chunksSections by chunkMap::keys

    override fun sendSpawnPackets(players: Collection<Player>) {
        update(players = players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        //todo: do
    }


    operator fun set(position: Position, data: WrappedBlockData) {
        val context = contexts.getOrNull()

        chunkMap.getOrPut(position.chunkSection) { arrayOfNulls(4096) }[position.chunkBlockPositionIndex] = data

        context?.chunksSections?.add(position.chunkSection)
    }

    operator fun set(location: Location, blockData: WrappedBlockData) = set(location.position, blockData)

    operator fun set(block: Block, blockData: WrappedBlockData) = set(block.position, blockData)

    operator fun set(position: BlockPosition, blockData: WrappedBlockData) = set(position.position, blockData)

    operator fun set(x: Int, y: Int, z: Int, blockData: WrappedBlockData) = set(Position(x, y, z), blockData)


    operator fun set(position: Position, blockData: BlockData) = set(position, createData(blockData))

    operator fun set(location: Location, blockData: BlockData) = set(location.position, createData(blockData))

    operator fun set(block: Block, blockData: BlockData) = set(block.position, blockData)

    operator fun set(position: BlockPosition, blockData: BlockData) = set(position.position, blockData)

    operator fun set(x: Int, y: Int, z: Int, blockData: BlockData) = set(Position(x, y, z), blockData)


    operator fun set(location: Location, material: Material) = set(location, material.createBlockData())

    operator fun set(block: Block, material: Material) = set(block.position, material.createBlockData())

    operator fun set(position: BlockPosition, material: Material) = set(position.position, material.createBlockData())

    operator fun set(x: Int, y: Int, z: Int, material: Material) = set(Position(x, y, z), material.createBlockData())


    fun getOrCreate(position: Position) = get(position) ?: set(position, AirBlockData)

    fun getOrCreate(location: Location) = get(location) ?: set(location, AirBlockData)

    fun getOrCreate(block: Block) = get(block) ?: set(block, AirBlockData)

    fun getOrCreate(position: BlockPosition) = get(position) ?: set(position, AirBlockData)

    fun getOrCreate(x: Int, y: Int, z: Int) = get(x, y, z) ?: set(x, y, z, AirBlockData)


    //getting
    operator fun get(position: Position) = chunkMap[position.chunkSection]?.get(position.chunkBlockPositionIndex)

    operator fun get(position: BlockPosition) = get(position.position)

    operator fun get(location: Location) = get(location.position)

    operator fun get(block: Block) = block.takeIf { it.world === world }?.let { get(it.location.position) }

    operator fun get(x: Int, y: Int, z: Int) = get(Position(x, y, z))

    //checks

    operator fun contains(position: Position) = chunkMap[position.chunkSection]?.get(position.chunkBlockPositionIndex) != null

    operator fun contains(position: BlockPosition) = contains(position.position)

    operator fun contains(location: Location) =
        contains(location.blockX, location.blockY, location.blockZ, location.world)

    fun contains(x: Int, y: Int, z: Int, world: World = this.world) =
        world === this.world && BlockPosition(x, y, z) in this

    //removing

    fun remove(position: Position) = get(position)?.let {
        chunkMap[position.chunkSection]!![position.chunkBlockPositionIndex] = null
        position.sendBlockData(if (removeByReplace) position.toBlock(world).blockData.wrap() else AirBlockData.wrap())
    }

    fun remove(position: BlockPosition) = remove(position.position)

    fun remove(location: Location) = remove(location.position)

    fun remove(block: Block) = remove(block.position)

    fun remove(x: Int, y: Int, z: Int) = remove(Position(x, y, z))

    fun clear(position: Position) = set(position, AirBlockData.wrap())

    fun clear(position: BlockPosition) = clear(position.position)

    fun clear(block: ProtocolBlock) = clear(block.position)

    fun clear(location: Location) = clear(location.position)

    fun clear(x: Int, y: Int, z: Int) = clear(Position(x, y, z))

    //filling
    fun fill(shape: Shape, mapper: (Location) -> WrappedBlockData?) =
        shape.mapNotNull { block -> mapper(block)?.let { set(block.position, it) } }

    fun fillData(shape: Shape, mapper: (Location) -> BlockData?) = fill(shape, mapper.map { it?.wrap() })

    fun fillMaterials(shape: Shape, mapper: (Location) -> Material?) =
        fill(shape, mapper.map { it?.createWrappedBlockData() })

    operator fun get(shape: Shape) = shape.asSequence().mapNotNull { get(it) }

    fun remove(shape: Shape) = shape.forEach { remove(it) }

    fun update(
        sections: Collection<Position> = chunksSections, players: Collection<Player> = viewers
    ) = sections
        .associateWith { chunk -> players.filter { it.isChunkLoaded(world, chunk) } }
        .forEach { (chunkSection, players) -> sendChunk(chunkSection, players) }

    fun update(sections: Collection<Position> = chunksSections, vararg players: Player) =
        update(sections, players.toList())

    fun sendChunk(chunkSection: Position, players: Collection<Player>) {
//        PacketMultiBlockChange().also{
//            it.setMeta("protocol", true)
//            it.flag = true
//            it.chunkSection = chunkSection
//            it.blocksByPositions = chunkMap[chunkSection]
//                ?.asSequence()
//                ?.associate { (position, data) -> position.blockPos to data } ?: emptyMap()
//        }.sendOrSendAll(players)
        batcher.batch(chunkSection, chunkMap[chunkSection] ?: arrayOfNulls(0), true, players.ifEmpty { viewers })
    }

    fun sendChunk(chunkSection: Position, vararg players: Player) = sendChunk(chunkSection, players.toList())

    fun batch(push: Boolean = true, block: ProtocolStructure.() -> Unit): ProtocolStructureBatch? {
        val new = !contexts.contains()
        val context = contexts.get()

        block(this)

        if (new) {
            contexts.remove()
            if (push) update(context.chunksSections)
        }

        return context
    }

    fun register() = apply {
        update()
        StructureProtocol.Structures.add(this)
    }

    override fun remove() {
        StructureProtocol.Structures.remove(this)
        super.remove()
    }

    override fun filterEvents(ctx: EventContext) {
        ctx.filter<ProtocolBlockEvent> { it.structure === this }
    }

    inner class ProtocolStructureBatch(val chunksSections: MutableSet<Position> = ObjectOpenHashSet())

    fun Position.sendBlockData(data: WrappedBlockData = get(this) ?: AirBlockData.wrap(), players: Collection<Player>) =
        PacketBlockChange().also {
            it.wrappedData = data
            it.position = blockPos
            it.setMeta("protocol", true)
        }.sendOrSendAll(players)

    fun Position.sendBlockData(data: WrappedBlockData = get(this) ?: AirBlockData.wrap(), vararg players: Player) =
        sendBlockData(data, players.toList())

    companion object {

        val Int.localBlockPosition
            get() = Position(this shr 8 and 0xF, this shr 4 and 0xF, this and 0xF)

    }

}

