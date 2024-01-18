package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardDisplayObjective
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardObjective
import dev.zephyr.protocol.scoreboard.type.ScoreboardHealthDisplay
import dev.zephyr.protocol.scoreboard.type.ScoreboardObjectiveAction
import dev.zephyr.util.component.literal
import dev.zephyr.util.component.toComponent
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import kotlin.properties.ReadWriteProperty

@KotlinOpens
class ProtocolScoreboardObjective(val name: String, val displaySlot: DisplaySlot = DisplaySlot.SIDEBAR) :
    ProtocolObject() {

    var titleString: String
        get() = title.literal()
        set(value) {
            title = value.toComponent()
        }
    var title by observable<Component>(Component.empty())
    var healthDisplay by observable(ScoreboardHealthDisplay.INTEGER)

    protected val teamMap: MutableMap<String, ProtocolScoreboardTeam> = hashMapOf()
    protected val scoreMap: MutableMap<String, ProtocolScoreboardScore> = hashMapOf()

    val teams by teamMap::values
    val scores by scoreMap::values

    override fun spawn(players: Collection<Player>) {
        super.spawn(players)

        teams.forEach { it.spawn(players) }
        scores.forEach { it.spawn(players) }
    }

    override fun destroy(players: Collection<Player>) {
        super.destroy(players)

        teamMap.values.forEach { it.destroy(players) }
        scoreMap.values.forEach { it.destroy(players) }
    }

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendObjective(ScoreboardObjectiveAction.CREATE)
        sendObjectiveDisplay()
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendObjective(ScoreboardObjectiveAction.DELETE)
        sendObjectiveDisplay()
    }

    fun getTeam(name: String) = teamMap[name]

    fun createTeam(name: String, vararg entries: String) = ProtocolScoreboardTeam(name, *entries).apply {
        removeTeam(name)

        teamMap[name] = this
        spawn(this@ProtocolScoreboardObjective.viewers)
    }

    fun removeTeam(name: String) = teamMap.remove(name)?.apply(ProtocolScoreboardTeam::remove)

    fun getOrCreateTeam(name: String, vararg entries: String) = getTeam(name) ?: createTeam(name, *entries)

    fun getScore(name: String) = scoreMap[name]

    fun createScore(name: String) = ProtocolScoreboardScore(name, this.name).apply {
        removeScore(name)

        scoreMap[name] = this
        spawn(this@ProtocolScoreboardObjective.viewers)
    }

    fun getOrCreateScore(name: String) = getScore(name) ?: createScore(name)

    fun removeScore(name: String) = scoreMap.remove(name)?.apply(ProtocolScoreboardScore::remove)

    fun sendObjective(action: ScoreboardObjectiveAction, players: Collection<Player>) =
        PacketScoreboardObjective().also {
            it.objectiveName = name
            it.healthDisplay = healthDisplay
            it.displayNameComponent = title
            it.action = action
        }.sendOrSendAll(players)

    fun sendObjective(action: ScoreboardObjectiveAction, vararg players: Player) =
        sendObjective(action, players.toList())

    fun sendObjectiveDisplay(players: Collection<Player>) = PacketScoreboardDisplayObjective().also {
        it.objectiveName = name
        it.slot = displaySlot
    }.sendOrSendAll(players)

    fun sendObjectiveDisplay(vararg players: Player) =
        sendObjectiveDisplay(players.toList())

    protected final fun <V> observable(value: V): ReadWriteProperty<Any?, V> {
        val observer: (V, V) -> Unit = { _, _ -> sendObjective(ScoreboardObjectiveAction.UPDATE) }
        return observable(value, observer = observer)
    }

}