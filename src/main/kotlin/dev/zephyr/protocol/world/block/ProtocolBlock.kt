package dev.zephyr.protocol.world.block

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.event.EventContext
import dev.zephyr.event.filter
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.packet.world.PacketBlockChange
import dev.zephyr.protocol.packet.world.PacketBlockDestroyAnimation
import dev.zephyr.protocol.unwrap
import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.world.event.block.ProtocolBlockEvent
import dev.zephyr.protocol.wrap
import dev.zephyr.util.block.AirBlockData
import dev.zephyr.util.block.positionHash
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

@KotlinOpens
abstract class ProtocolBlock(wrappedBlockData: WrappedBlockData = AirBlockData.wrap()) : ProtocolObject() {
    constructor(blockData: BlockData) : this(WrappedBlockData.createData(blockData))

    var damage by observable(.0) { _, damage -> sendBlockDamage(damage) }
    var wrappedBlockData by observable(wrappedBlockData) { _, data -> sendBlockData(data) }
    var blockData: BlockData
        get() = wrappedBlockData.unwrap()
        set(value) {
            wrappedBlockData = value.wrap()
        }
    var blockType: Material
        get() = wrappedBlockData.type
        set(value) {
            blockData = value.createBlockData()
        }

    abstract val location: Location
    abstract val position: Position

    val world get() = location.world
    val chunk get() = location.chunk

    val chunkPosition get() = position.chunk
    val chunkSectionPosition get() = position.chunkSection

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendBlockData(wrappedBlockData, players)
        sendBlockDamage(damage, players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendBlockData(location.block.blockData.wrap(), players)
        sendBlockDamage(.0, players)
    }

    fun sendBlockData(data: WrappedBlockData = this.wrappedBlockData, players: Collection<Player> = viewers) =
        PacketBlockChange().also {
            it.wrappedData = data
            it.position = position.blockPos
            it.setMeta("protocol", true)
        }.sendOrSendAll(players)

    fun sendBlockData(data: WrappedBlockData = this.wrappedBlockData, vararg players: Player) =
        sendBlockData(data, players.toList())

    fun sendBlockDamage(damage: Double = this.damage, players: Collection<Player> = viewers) =
        PacketBlockDestroyAnimation().also {
            it.entityId = position.positionHash.toInt()
            it.percent = damage
            it.blockPosition = position.blockPos
            it.setMeta("protocol", true)
        }.sendOrSendAll(players)

    fun sendBlockDamage(damage: Double = this.damage, vararg players: Player) =
        sendBlockDamage(damage, players.toList())

    fun clear() = apply {
        blockData = AirBlockData
        damage = .0
    }

    fun copy(protocolBlock: ProtocolBlock) = apply {
        this.damage = protocolBlock.damage
        this.blockData = protocolBlock.blockData
    }

    override fun filterEvents(ctx: EventContext) {
        ctx.filter<ProtocolBlockEvent> { it.block === this }
    }

}