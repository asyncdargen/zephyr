package dev.zephyr.protocol

import dev.zephyr.event.EventContext
import dev.zephyr.event.GlobalEventContext
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.task.GlobalTaskContext
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.isLazyInitialized
import org.bukkit.entity.Player

@KotlinOpens
abstract class ProtocolObject  {

    val eventContext by lazy { GlobalEventContext.fork(false).apply(this::filterEvents) }
    val taskContext by lazy { GlobalTaskContext.fork(false) }

    val viewers: MutableCollection<Player> = concurrentSetOf()

    val hasViewers get() = viewers.isNotEmpty()

    fun isSpawned(player: Player) = player in viewers

    @Synchronized
    fun spawn(players: Collection<Player>) {
        if (players.isEmpty()) {
            return
        }

        viewers.addAll(players)

        sendSpawnPackets(players)
    }

    fun spawn(vararg players: Player) = spawn(players.toList())

    abstract fun sendSpawnPackets(players: Collection<Player>)

    fun sendSpawnPackets(vararg players: Player) =
        sendSpawnPackets(players.toList())

    @Synchronized
    fun destroy(players: Collection<Player>) {
        if (players.isEmpty()) {
            return
        }

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
        destroy(viewers)
        if (this::eventContext.isLazyInitialized) {
            eventContext.close()
        }
        if (this::taskContext.isLazyInitialized) {
            taskContext.close()
        }

    }

    fun handleRemove() {}

    fun filterEvents(ctx: EventContext) {}

    protected fun ProtocolPacket.sendOrSendAll(players: Collection<Player>) {
        send(players.ifEmpty { viewers })
    }

}