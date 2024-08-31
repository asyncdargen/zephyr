package dev.zephyr.protocol

import dev.zephyr.event.EventContext
import dev.zephyr.event.GlobalEventContext
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.task.GlobalTaskContext
import dev.zephyr.util.bukkit.players
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.cast
import org.bukkit.entity.Player
import java.util.concurrent.locks.ReentrantLock

@KotlinOpens
abstract class ProtocolObject {

    final var hasTaskContext: Boolean = false
        private set
    final var hasEventContext: Boolean = false
        private set

    val eventContext by lazy { hasEventContext = true; GlobalEventContext.fork(false).apply(this::filterEvents) }
    val taskContext by lazy { hasTaskContext = true; GlobalTaskContext.fork(false) }

    var spawnHandler: ProtocolObject.(Player) -> Unit = { }
    var destroyHandler: ProtocolObject.(Player) -> Unit = { }

    val viewers: MutableCollection<Player> = concurrentSetOf()
    val loaders: MutableCollection<Player> = concurrentSetOf()

    val hasViewers get() = viewers.isNotEmpty()

    fun isSpawned(player: Player) = player in viewers
    fun isLoaded(player: Player) = player in loaders

    fun broadcastSpawn() = spawn(players())

    @Synchronized
    fun load(players: Collection<Player>) {
        loaders.addAll(players)
    }

    fun load(vararg players: Player) =
        load(players.toList())

    @Synchronized
    fun unload(players: Collection<Player>) {
        loaders.removeAll(players.toSet())
    }

    fun unload(vararg players: Player) =
        unload(players.toList())

    @Synchronized
    fun spawn(players: Collection<Player>) {
        if (players.isEmpty()) {
            return
        }

        viewers.addAll(players)

        sendSpawnPackets(players)
        players.forEach { spawnHandler(it) }
    }

    fun spawn(vararg players: Player) = spawn(players.toList())

    abstract fun sendSpawnPackets(players: Collection<Player>)

    fun sendSpawnPackets(vararg players: Player) =
        sendSpawnPackets(players.toList())

    fun broadcastDestroy() = destroy(players())

    @Synchronized
    fun destroy(players: Collection<Player>) {
        if (players.isEmpty()) {
            return
        }

        players.forEach { destroyHandler(it) }

        sendDestroyPackets(players)

        viewers.removeAll(players.toSet())
    }

    fun destroy(vararg players: Player) =
        destroy(players.toList())

    abstract fun sendDestroyPackets(players: Collection<Player>)

    fun sendDestroyPackets(vararg players: Player) =
        sendDestroyPackets(players.toList())

    @Synchronized
    fun remove() {
        handleRemove()
        destroy(loaders)
        unload(loaders)

        if (hasEventContext) {
            eventContext.close()
        }
        if (hasTaskContext) {
            taskContext.close()
        }

    }

    fun handleRemove() {}

    fun filterEvents(ctx: EventContext) {}

    protected fun ProtocolPacket.sendOrSendAll(players: Collection<Player>) {
        send(players.ifEmpty { viewers })
    }

}

infix fun <O : ProtocolObject> O.spawn(handler: O.(Player) -> Unit) = apply { spawnHandler = handler.cast() }

infix fun <O : ProtocolObject> O.destroy(handler: O.(Player) -> Unit) = apply { destroyHandler = handler.cast() }