package dev.zephyr.protocol.world

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.asBlockPosition
import dev.zephyr.protocol.getChunkSection
import dev.zephyr.protocol.packet.block.PacketBlockChange
import dev.zephyr.protocol.packet.block.PacketBlockDestroyAnimation
import dev.zephyr.util.block.AirBlockData
import dev.zephyr.util.bukkit.block
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import dev.zephyr.util.kotlin.safeCast
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolBlock(location: Location, wrappedBlockData: WrappedBlockData) : ProtocolObject() {
    constructor(location: Location, blockData: BlockData) : this(location, WrappedBlockData.createData(blockData))

    var blockDamage by observable(.0) { _, damage -> sendBlockDamage(damage) }
    var wrappedBlockData by observable(wrappedBlockData) { _, data -> sendBlockData(data) }
    var blockData
        get() = wrappedBlockData.handle.safeCast<BlockData>()
        set(value) {
            wrappedBlockData = WrappedBlockData.createData(value)
        }
    var blockType: Material
        get() = wrappedBlockData.type
        set(value) {
            blockData = value.createBlockData()
        }

    val location = location.block()
    val world by location::world
    val chunk by location::chunk
    val position = location.asBlockPosition()
    val chunkSection get() = position.getChunkSection()

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendBlockData(wrappedBlockData, players)
        sendBlockDamage(blockDamage, players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendBlockData(WrappedBlockData.createData(location.block.blockData), players)
        sendBlockDamage(.0, players)
    }

    fun sendBlockData(data: WrappedBlockData, players: Collection<Player>) = PacketBlockChange().also {
        it.wrappedData = data
        it.position = location.asBlockPosition()
        it.setMeta("protocol", true)
    }.sendOrSendAll(players)

    fun sendBlockData(data: WrappedBlockData, vararg players: Player) =
        sendBlockData(data, players.toList())

    fun sendBlockDamage(damage: Double = this.blockDamage, players: Collection<Player>) = PacketBlockDestroyAnimation().also {
        it.entityId = ((position.x and 0xFFF shl 20) or (position.z and 0xFFF shl 8) or (position.y and 0xFF))
        it.percent = damage
        it.position = location.asBlockPosition()
        it.setMeta("protocol", true)
    }.sendOrSendAll(players)

    fun sendBlockDamage(damage: Double = this.blockDamage, vararg players: Player) =
        sendBlockDamage(damage, players.toList())

    fun clear() = apply {
        blockData = AirBlockData
        blockDamage = .0
    }

    fun register() = apply { StructureProtocol.Blocks.add(this) }

    fun copy(protocolBlock: ProtocolBlock) = apply {
        this.blockDamage = protocolBlock.blockDamage
        this.blockData = protocolBlock.blockData
    }

    override fun remove() {
        StructureProtocol.Blocks.remove(this)
        super.remove()
    }

}