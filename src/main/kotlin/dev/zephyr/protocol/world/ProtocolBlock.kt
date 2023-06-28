package dev.zephyr.protocol.world

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.asProtocolBlockPosition
import dev.zephyr.protocol.packet.block.PacketBlockChange
import dev.zephyr.protocol.packet.block.PacketBlockDestroyAnimation
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import org.bukkit.Location
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolBlock(val location: Location, blockData: BlockData) : ProtocolObject() {

    var blockDamage by observable(.0) { _, damage -> sendBlockDamage(damage) }
    var blockData by observable(blockData) { _, data -> sendBlockData(data) }
    val chunk by location::chunk

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendBlockData(blockData, players)
        sendBlockDamage(blockDamage, players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendBlockData(location.block.blockData, players)
        sendBlockDamage(.0, players)
    }

    fun sendBlockData(data: BlockData, players: Collection<Player>) = PacketBlockChange().also {
        it.data = data
        it.position = location.asProtocolBlockPosition()
    }.sendOrSendAll(players)

    fun sendBlockData(data: BlockData, vararg players: Player) =
        sendBlockData(data, players.toList())

    fun sendBlockDamage(damage: Double, players: Collection<Player>) = PacketBlockDestroyAnimation().also {
        it.percent = damage
        it.position = location.asProtocolBlockPosition()
    }.sendOrSendAll(players)

    fun sendBlockDamage(damage: Double, vararg players: Player) =
        sendBlockDamage(damage, players.toList())


    fun register() = apply { StructureProtocol.Blocks.add(this) }

    override fun remove() {
        StructureProtocol.Blocks.remove(this)
        super.remove()
    }

}