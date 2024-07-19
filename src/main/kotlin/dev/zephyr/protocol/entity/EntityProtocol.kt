package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction
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
import org.bukkit.World

object EntityProtocol {

    var AutoRegister = false

    val Entities: MutableMap<World, MutableMap<Int, ProtocolEntity>> = concurrentHashMapOf()

    init {
        Protocol.onReceive(PacketPlayInType.USE_ENTITY, async = true) {
            val entityId = packet.integers.read(0)
            val world = player.world
            val entity = Entities.getOrPut(world) { concurrentHashMapOf() }[entityId]

            val actionRaw = packet.enumEntityUseActions.read(0)?.action ?: return@onReceive
            val action = when (actionRaw) {
                EntityUseAction.ATTACK -> EntityInteract.ATTACK
                else -> EntityInteract.INTERACT
            }

            PlayerFakeEntityInteractEvent(player, action, entityId, entity).callEvent()

            entity ?: return@onReceive
            checkOrSetDelay("${entityId}_${player.name}_click", 20) {
                if (entity.isSpawned(player)) entity.clickHandler(entity,player,action)
            }
        }
        on<PlayerChunkUnloadEvent> {
            getEntitiesInChunk(chunk)?.filter { it.isSpawned(player) }?.forEach { it.destroy(player) }
        }

        everyAsync(2, 2) {
            Entities.values.forEach {
                it.values.forEach(ProtocolEntity::refreshViewers)
            }
        }
    }

    operator fun contains(entity: ProtocolEntity) = isRegistered(entity)

    operator fun get(chunk: Chunk) = getEntitiesInChunk(chunk)

    operator fun get(world: World) = Entities[world]

    fun isRegistered(world: World, id: Int) = Entities[world]?.contains(id) ?: false

    fun isRegistered(entity: ProtocolEntity) = isRegistered(entity.world,entity.entityId)

    fun getEntitiesInChunk(world: World, chunkX: Int, chunkZ: Int) =
        Entities[world]?.values?.filter { it.location.blockX shr 4 == chunkX && it.location.blockZ shr 4 == chunkZ }

    fun getEntitiesInWorld(world: World) = Entities[world]?.values

    fun getEntitiesInChunk(chunk: Chunk) = getEntitiesInChunk(chunk.world,chunk.x, chunk.z)
}