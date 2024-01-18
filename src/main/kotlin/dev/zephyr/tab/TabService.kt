package dev.zephyr.tab

import com.comphenix.protocol.wrappers.EnumWrappers
import dev.zephyr.protocol.packet.player.PacketPlayerInfoUpdate
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.collection.takeIfNotEmpty
import dev.zephyr.util.concurrent.use
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

object TabService {

    var setupBlock: TabPlayer.() -> Unit = {}

    val queueLock: Lock = ReentrantLock()
    val queued = concurrentSetOf<Player>()

    val players = concurrentHashMapOf<Player, TabPlayer>()

    fun setup(block: TabPlayer.() -> Unit = setupBlock) {
        setupBlock = block

        on<PlayerJoinEvent> { queueLock.use { queued.add(player) } }
        on<PlayerQuitEvent> { players.remove(player)?.remove() }

        on<PlayerToggleSneakEvent> { getSafe(player)?.container?.resneakTag(isSneaking) }

        everyAsync(1, 1) {
            var queued: Collection<TabPlayer> = emptyList()
            var queuedPlayers: Collection<Player> = emptyList()
            queueLock.use {
                queuedPlayers = this.queued.filter(Player::isOnline)
                queued = queuedPlayers.map(::TabPlayer).onEach { it.setupBlock(); players[it.player] = it }
                this.queued.clear()
            }

            players.values.forEach { if (!it.dirtyTeam) it.container.getOrCreateTeam().spawn(queuedPlayers) }
            players.values.forEach { tab -> tab.update() }
            collectProfilePacket(queued.isNotEmpty())?.broadcast()
        }
    }

    fun updater(interval: Int = 20, block: TabPlayer.() -> Unit) =
        everyAsync(interval, interval) { players.values.forEach(block) }

    fun getSafe(player: Player) = players[player]

    operator fun get(player: Player) = getSafe(player)!!

    private fun collectProfilePacket(force: Boolean = false): PacketPlayerInfoUpdate? =
        players.values.filter { force || it.dirtyName }
            .map { it.dirtyName = false; it.container.getOrCreateProfile() }
            .takeIfNotEmpty()
            ?.let { PacketPlayerInfoUpdate.many(it, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME) }

}