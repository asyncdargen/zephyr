package dev.zephyr.protocol.scoreboard

import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardScore
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import kotlin.properties.ReadWriteProperty

@KotlinOpens
class ProtocolScoreboardScore(val name: String, val objectiveName: String) : ProtocolObject() {

    var score by observable(0)

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendUpdates(ScoreboardAction.CHANGE, players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendUpdates(ScoreboardAction.REMOVE, players)
    }

    fun sendUpdates(action: ScoreboardAction, players: Collection<Player>) = PacketScoreboardScore().also {
        it.scoreName = name
        it.objectiveName = objectiveName
        it.action = action
        it.score = score
    }.sendOrSendAll(players)

    fun sendUpdates(action: ScoreboardAction, vararg players: Player) =
        sendUpdates(action, players.toList())

    protected final fun <V> observable(value: V): ReadWriteProperty<Any?, V> {
        val observer: (V, V) -> Unit =
            { _, _ -> sendUpdates(ScoreboardAction.CHANGE) } //fuck kotlin with recursion bug!!!!
        return dev.zephyr.util.kotlin.observable(value, observer = observer)
    }

}