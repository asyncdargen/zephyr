package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction
import dev.zephyr.Zephyr
import dev.zephyr.protocol.PacketPlayInType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.entity.event.PlayerFakeEntityInteractEvent
import dev.zephyr.protocol.entity.type.EntityInteract
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.time.checkOrSetDelay
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.World

object EntityProtocol {

    var AutoRegister = false

    val EntitiesMap: MutableMap<Int, ProtocolEntity> = concurrentHashMapOf()
    val Entities by EntitiesMap::values
    val EntitiesSequence
        get() = Entities.asSequence()

    fun initialize() {
        Protocol.onReceive(PacketPlayInType.USE_ENTITY, async = true) {
            val entityId = packet.integers.read(0)
            val entity = EntitiesMap[entityId]

            val actionRaw = packet.enumEntityUseActions.read(0)?.action ?: return@onReceive
            val action = when (actionRaw) {
                EntityUseAction.ATTACK -> EntityInteract.ATTACK
                else -> EntityInteract.INTERACT
            }

            PlayerFakeEntityInteractEvent(player, action, entityId, entity).callEvent()

            entity ?: return@onReceive
            if (!entity.isSpawned(player)) {
                Zephyr.Logger.warning("Player ${player.name} clicked to protocol entity $entityId not spawned from him!")
            } else checkOrSetDelay("${entityId}_${player.name}_click", 20) {
                entity.clickHandler(entity, player, action)
            }
        }
//
//        on<PlayerTeleportEvent> {
//            val entities = setOf(player)
//            Entities.forEach { it.destroy(entities) }
//
//            setDelay("entity_protocol_teleport_${player.name}", 1000)
//        }
        on<PlayerChunkUnloadEvent> {
            getEntitiesInChunk(chunk)
                .filter { it.isSpawned(player) }
                .forEach { it.destroy(player) }
        }

        everyAsync(2, 2) {
            EntitiesSequence.forEach(ProtocolEntity::refreshViewers)
        }
    }

    operator fun contains(id: Int) = isRegistered(id)

    operator fun contains(entity: ProtocolEntity) = isRegistered(entity)

    operator fun get(world: World) = getEntitiesInWorld(world)

    operator fun get(chunk: Chunk) = getEntitiesInChunk(chunk)

    operator fun get(id: Int) = EntitiesMap[id]

    fun isRegistered(id: Int) = id in EntitiesMap

    fun isRegistered(entity: ProtocolEntity) = isRegistered(entity.entityId)

    fun getInRange(location: Location, distance: Double) =
        get(location.world).filter { it.location.distance(location) <= distance }

    fun getEntitiesInChunk(chunkX: Int, chunkZ: Int) =
        EntitiesSequence.filter { it.location.blockX shr 4 == chunkX && it.location.blockZ shr 4 == chunkZ }

    fun getEntitiesInWorld(world: World) = EntitiesSequence.filter { it.world === world }

    fun getEntitiesInChunk(chunk: Chunk) = getEntitiesInChunk(chunk.x, chunk.z)


}