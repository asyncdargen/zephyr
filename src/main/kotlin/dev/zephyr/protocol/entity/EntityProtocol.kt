package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction
import dev.zephyr.protocol.PacketPlayInType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.entity.event.PlayerFakeEntityInteractEvent
import dev.zephyr.protocol.entity.type.EntityInteract
import dev.zephyr.protocol.world.*
import dev.zephyr.protocol.world.event.chunk.PlayerChunkLoadEvent
import dev.zephyr.protocol.world.event.chunk.PlayerChunkUnloadEvent
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.time.checkOrSetDelay
import org.bukkit.Chunk
import org.bukkit.World
import org.bukkit.event.player.PlayerChangedWorldEvent

object EntityProtocol {

    val Entities: MutableMap<Int, ProtocolEntity> = concurrentHashMapOf()
    val EntitiesByPositions: MutableMap<World, MutableMap<ChunkPosition,MutableMap<Int, ProtocolEntity>>> = concurrentHashMapOf()

    init {
        Protocol.onReceive(PacketPlayInType.USE_ENTITY, async = true) {
            val entityId = packet.integers.read(0)
            val entity = Entities[entityId]
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

        on<PlayerChunkLoadEvent> {
            getEntitiesInChunk(chunk)?.forEach { it.load(player) }
        }

        on<PlayerChunkUnloadEvent> {
            getEntitiesInChunk(chunk)?.forEach { it.unload(player) }
        }

        on<PlayerChangedWorldEvent> {
            getEntitiesInWorld(from)?.forEach { it.unload(player) }
        }

        everyAsync(2, 2) {
            Entities.values.forEach {
                updatePosition(it)
                it.refreshViewers()
            }
        }
    }

    operator fun contains(entity: ProtocolEntity) = isRegistered(entity)

    operator fun get(chunk: Chunk) = getEntitiesInChunk(chunk)

    operator fun get(world: World) = EntitiesByPositions[world]

    fun isRegistered(id: Int) = Entities.containsKey(id)

    fun isRegistered(entity: ProtocolEntity) = isRegistered(entity.entityId)

    fun getEntitiesInChunk(world: World, position: ChunkPosition) = EntitiesByPositions[world]?.get(position)?.values

    fun getEntitiesInWorld(world: World) = EntitiesByPositions[world]?.values?.flatMap { it.values }

    fun getEntitiesInChunk(chunk: Chunk) = getEntitiesInChunk(chunk.world,chunk.chunkPosition)

    fun updatePosition(entity: ProtocolEntity) {
        val entityId = entity.entityId
        if (entity.worldIsDirty || entity.chunkIsDirty) {
            val oldWorld = entity.latestWorldSnapshot
            val newWorld = entity.world
            val oldChunk = entity.latestChunkSnapshot
            val newChunk = entity.location.chunkSectionPosition
            EntitiesByPositions[oldWorld]?.get(oldChunk)?.remove(entityId)
            EntitiesByPositions.getOrPut(newWorld) { concurrentHashMapOf() }
                .getOrPut(newChunk) { concurrentHashMapOf() }[entityId] = entity
            entity.latestChunkSnapshot = newChunk
            entity.latestWorldSnapshot = newWorld
            entity.chunkIsDirty = false
            entity.worldIsDirty = false
            entity.refreshLoaders()
        }
    }
}