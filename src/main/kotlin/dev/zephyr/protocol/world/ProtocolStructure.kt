package dev.zephyr.protocol.world

import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import com.comphenix.protocol.wrappers.WrappedBlockData.createData
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.asBlockPosition
import dev.zephyr.protocol.asLocation
import dev.zephyr.protocol.getChunkSection
import dev.zephyr.protocol.packet.block.PacketMultiBlockChange
import dev.zephyr.util.bukkit.at
import dev.zephyr.util.bukkit.isChunkLoaded
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.concurrent.threadLocal
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.map
import dev.zephyr.util.kotlin.mapFully
import dev.zephyr.util.shape.Shape
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

typealias StructureBlock = ProtocolStructure.ProtocolStructureBlock
private typealias ChunkBlockMap = MutableMap<ChunkSection, MutableMap<BlockPosition, StructureBlock>>

@KotlinOpens
class ProtocolStructure(val world: World) : ProtocolObject() {

    protected val contexts = threadLocal { mutableSetOf<ChunkSection>() }

    val chunkMap: ChunkBlockMap = concurrentHashMapOf()
    val chunksSections by chunkMap::keys

    override fun sendSpawnPackets(players: Collection<Player>) {
        update(players = players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        //todo: do
    }

    //setting
    fun put(block: ProtocolBlock) = set(block.position, block)

    operator fun set(blockPosition: BlockPosition, block: ProtocolBlock) = StructureBlock(block).apply {
        val context = contexts.takeIf { it.contains() }?.get()
        context ?: removeBlock(blockPosition)

        chunkMap.getOrPut(block.chunkSection, ::concurrentHashMapOf)[position] = this
        context?.add(chunkSection)
    }

    operator fun set(location: Location, block: ProtocolBlock) =
        set(location.asBlockPosition(), block)

    operator fun set(bukkitBlock: Block, block: ProtocolBlock) =
        set(bukkitBlock.location, block)

    operator fun set(x: Number, y: Number, z: Number, block: ProtocolBlock) =
        set(world.at(x, y, z), block)

    operator fun set(location: Location, blockData: WrappedBlockData) =
        put(StructureBlock(location, blockData))

    operator fun set(bukkitBlock: Block, blockData: WrappedBlockData) =
        set(bukkitBlock.location, blockData)

    operator fun set(blockPosition: BlockPosition, blockData: WrappedBlockData) =
        set(blockPosition.asLocation(world), blockData)


    operator fun set(location: Location, blockData: BlockData) =
        set(location, createData(blockData))

    operator fun set(bukkitBlock: Block, blockData: BlockData) =
        set(bukkitBlock.location, blockData)

    operator fun set(blockPosition: BlockPosition, blockData: BlockData) =
        set(blockPosition.asLocation(world), blockData)

    operator fun set(x: Number, y: Number, z: Number, blockData: BlockData) =
        set(world.at(x, y, z), blockData)

    operator fun set(location: Location, material: Material) =
        set(location, material.createBlockData())

    operator fun set(bukkitBlock: Block, material: Material) =
        set(bukkitBlock.location, material.createBlockData())

    operator fun set(blockPosition: BlockPosition, material: Material) =
        set(blockPosition, material.createBlockData())

    operator fun set(x: Number, y: Number, z: Number, material: Material) =
        set(x, y, z, material.createBlockData())


    //getting
    operator fun get(blockPosition: BlockPosition) = chunkMap[blockPosition.getChunkSection()]?.get(blockPosition)

    operator fun get(location: Location) = get(location.asBlockPosition())

    operator fun get(bukkitBlock: Block) = bukkitBlock.takeIf { it.world === world }?.location?.let { get(it) }

    operator fun get(x: Number, y: Number, z: Number) = get(BlockPosition(x.toInt(), y.toInt(), z.toInt()))

    //checks
    operator fun contains(blockPosition: BlockPosition) =
        blockPosition in (chunkMap[blockPosition.getChunkSection()] ?: emptyMap())

    operator fun contains(location: Location) =
        location.world === world && location.asBlockPosition() in this

    fun contains(x: Number, y: Number, z: Number, world: World = this.world) =
        world === this.world && BlockPosition(x.toInt(), y.toInt(), z.toInt()) in this

    //removing

    fun remove(blockPosition: BlockPosition) = get(blockPosition)?.clear()

    fun remove(location: Location) = get(location)?.clear()

    fun remove(bukkitBlock: Block) = get(bukkitBlock)?.clear()

    fun remove(x: Number, y: Number, z: Number) = get(x, y, z)?.clear()

    fun removeBlock(position: BlockPosition) =
        chunkMap[position.getChunkSection()]
            ?.remove(position)
            ?.sendDestroyPackets()

    fun removeBlock(block: ProtocolBlock) = removeBlock(block.position)

    fun removeBlock(location: Location) = removeBlock(location.asBlockPosition())

    fun removeBlock(x: Number, y: Number, z: Number) =
        removeBlock(BlockPosition(x.toInt(), y.toInt(), z.toInt()))

    //filling
    fun fillBlocks(shape: Shape, mapper: (Block) -> StructureBlock?) =
        shape.asSequence().mapNotNull(mapper).apply { forEach { put(it) } }

    fun fill(shape: Shape, mapper: (Block) -> WrappedBlockData?) =
        fillBlocks(shape, mapper.mapFully { block, data -> data?.let { StructureBlock(block.location, it) } })

    fun fillData(shape: Shape, mapper: (Block) -> BlockData?) =
        fill(shape, mapper.map { WrappedBlockData.createData(it) })

    fun fillMaterials(shape: Shape, mapper: (Block) -> Material?) =
        fillData(shape, mapper.map { it?.let(Material::createBlockData) })

    operator fun get(shape: Shape) = shape.asSequence().mapNotNull { get(it) }

    fun remove(shape: Shape) = shape.forEach { remove(it) }

    fun update(
        sections: Collection<ChunkSection> = chunksSections,
        players: Collection<Player> = viewers
    ) {
        sections
            .asSequence()
            .map { it.pointer(world) }
            .associateWith { chunk -> players.filter { it.isChunkLoaded(chunk) } }
            .forEach { (chunkSection, players) ->
                sendChunk(chunkSection.section, players)
                chunkMap[chunkSection.section]?.values
                    ?.asSequence()
                    ?.filter { it.blockDamage >= 0.1 }
                    ?.forEach { it.sendBlockDamage(players = players) }
            }
    }

    fun update(sections: Collection<ChunkSection> = chunksSections, vararg players: Player) =
        update(sections, players.toList())

    fun sendChunk(chunkSection: ChunkSection, players: Collection<Player>) = PacketMultiBlockChange().also {
        it.flag = true
        it.chunkSection = chunkSection
        it.blocksByPositions = chunkMap[chunkSection]
            ?.values
            ?.associate { block -> block.position to block.wrappedBlockData } ?: emptyMap()
    }.sendOrSendAll(players)

    fun sendChunk(chunkSection: ChunkSection, vararg players: Player) =
        sendChunk(chunkSection, players.toList())

    fun batch(block: ProtocolStructure.() -> Unit): ProtocolStructure {
        val new = !contexts.contains()
        val context = contexts.get()

        block(this)

        if (new) {
            contexts.remove()
            update(context)
        }

        return this
    }

    fun register() = apply {
        update()
        StructureProtocol.Structures.add(this)
    }

    override fun remove() {
        StructureProtocol.Structures.remove(this)
        super.remove()
    }

    @KotlinOpens
    inner class ProtocolStructureBlock(location: Location, blockData: WrappedBlockData) :
        ProtocolBlock(location, blockData) {
        constructor(block: ProtocolBlock) : this(block.location, block.wrappedBlockData) {
            blockDamage = block.blockDamage
        }

        val structure get() = this@ProtocolStructure

        override fun sendBlockDamage(damage: Double, players: Collection<Player>) {
            if (players !== viewers && players.isNotEmpty() || !contexts.contains()) {
                super.sendBlockDamage(damage, players)
            }
        }

        override fun sendBlockData(data: WrappedBlockData, players: Collection<Player>) {
            if (players !== viewers && players.isNotEmpty() || !contexts.contains()) {
                super.sendBlockData(data, players)
            }
        }

        override var viewers = this@ProtocolStructure.viewers

        override fun hashCode() = position.hashCode()

    }

}