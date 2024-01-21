package dev.zephyr.hook.vehicle

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.ProtocolVehicle
import dev.zephyr.protocol.packet.entity.PacketEntityMount
import dev.zephyr.util.bukkit.clearAngles
import dev.zephyr.util.bukkit.diff
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolPlayerVehicle(val player: Player) : ProtocolVehicle {

    override val mounts = mutableSetOf<ProtocolEntity>()

    override fun sendMounts(players: Collection<Player>) = PacketEntityMount().also {
        it.entityId = player.entityId
        it.entities = (mounts.map(ProtocolEntity::entityId) + player.passengers.map(Entity::getEntityId)).toIntArray()
    }.send(players)

    override fun syncMounts() {
        mounts.forEach {
            it.location = player.location.diff(y = player.height).clearAngles()
            sendMounts(mounts.flatMap(ProtocolEntity::viewers).distinct())
        }
    }

    override fun mount(entities: Collection<ProtocolEntity>) {
        entities.forEach { it.vehicle = this }
        mounts.addAll(entities)
        syncMounts()
        sendMounts(entities.flatMap(ProtocolEntity::viewers).distinct())
    }

    override fun unmount(entities: Collection<ProtocolEntity>) {
        mounts.removeAll(entities)
        entities.firstOrNull()?.viewers?.let { sendMounts(it) }
    }

    fun clear() = unmount(*mounts.toTypedArray())

}