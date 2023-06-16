package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction
import dev.zephyr.Zephyr
import dev.zephyr.protocol.PacketPlayInType
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.entity.type.EntityInteract
import dev.zephyr.extensions.bukkit.after
import dev.zephyr.extensions.bukkit.on
import dev.zephyr.extensions.concurrentHashMapOf
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent

object EntityProtocol {

    var AutoRegister = false
    var ViewDistance = 70.0
//    var AllowedClickDistance = 7.0

    @get:Synchronized
    val EntitiesMap: MutableMap<Int, ProtocolEntity> = concurrentHashMapOf()
    val Entities by EntitiesMap::values
    val EntitiesSequence
        get() = Entities.asSequence()

    init {
        Protocol.onReceive(PacketPlayInType.USE_ENTITY) {
            val entityId = packet.integers.read(0)
            val entity = EntitiesMap[entityId] ?: return@onReceive

            val actionRaw = packet.enumEntityUseActions.read(0)?.action ?: return@onReceive
            val action = when (actionRaw) {
                EntityUseAction.ATTACK -> EntityInteract.ATTACK
                EntityUseAction.INTERACT -> EntityInteract.INTERACT
                else -> return@onReceive
            }

            val distance = player.location.distance(entity.location)
           /* if (distance > AllowedClickDistance)
                Zephyr.Logger.warning("Player ${player.name} clicked to protocol entity $entityId with $distance distance!")
            else */if (!entity.isSpawned(player))
                Zephyr.Logger.warning("Player ${player.name} clicked to protocol entity $entityId not spawned from him!")
            else after {
                entity.clickHandler(player, action)
            }
        }

        Protocol.onSend(PacketPlayOutType.MAP_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            getEntitiesInChunk(chunkX, chunkZ)
                .forEach { it.spawn(player) }
        }
        Protocol.onSend(PacketPlayOutType.UNLOAD_CHUNK) {
            val chunkX = packet.integers.read(0)
            val chunkZ = packet.integers.read(1)

            getEntitiesInChunk(chunkX, chunkZ)
                .filter { it.isSpawned(player) }
                .forEach { it.destroy(player) }
        }
        on<PlayerChangedWorldEvent> {
            Entities
                .filter { it.world !== player.world && it.isSpawned(player) }
                .forEach { it.destroy(player) }
        }
        on<PlayerQuitEvent> { Entities.forEach { it.destroy(player) } }
    }

    operator fun contains(id: Int) = isRegistered(id)

    operator fun contains(entity: ProtocolEntity) = isRegistered(entity)

    operator fun get(world: World) = getEntitiesInWorld(world)

    operator fun get(chunk: Chunk) = getEntitiesInChunk(chunk)

    operator fun get(id: Int) = EntitiesMap[id]

    fun isRegistered(id: Int) = id in EntitiesMap

    fun isRegistered(entity: ProtocolEntity) = isRegistered(entity.id)

    fun getInRange(location: Location, distance: Double) =
        get(location.world).filter { it.location.distance(location) <= distance }

    fun getEntitiesInChunk(chunkX: Int, chunkZ: Int) =
        EntitiesSequence.filter { it.location.blockX shr 4 == chunkX && it.location.blockZ shr 4 == chunkZ }

    fun getEntitiesInWorld(world: World) = EntitiesSequence.filter { it.world === world }

    fun getEntitiesInChunk(chunk: Chunk) = getEntitiesInChunk(chunk.x, chunk.z)


}