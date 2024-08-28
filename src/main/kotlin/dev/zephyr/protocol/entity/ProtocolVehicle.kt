package dev.zephyr.protocol.entity

import org.bukkit.entity.Player
import java.util.concurrent.ConcurrentHashMap

interface ProtocolVehicle {

    val mounts: MutableSet<ProtocolEntity>

    fun sendMounts(players: Collection<Player>)

    fun syncMounts()

    fun mount(entities: Collection<ProtocolEntity>)

    fun mount(vararg entities: ProtocolEntity) = mount(entities.toList())

    fun unmount(entities: Collection<ProtocolEntity>)

    fun unmount(vararg entities: ProtocolEntity) = unmount(entities.toList())

}