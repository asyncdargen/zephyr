package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamAction
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import kotlin.properties.ReadWriteProperty

@KotlinOpens
class ProtocolScoreboardTeam(val name: String, entities: List<String>) : ProtocolObject() {
    constructor(name: String, vararg entities: String) : this(name, entities.toList())
    constructor(uuid: UUID, vararg entities: String) : this(
        uuid.toString().replace("-", "").substring(16),
        entities.toList()
    )

    val entities: MutableList<String> = entities.toMutableList() //todo: make observable list

    var collision by observable(ScoreboardTeamCollision.ALWAYS)
    var visibility by observable(ScoreboardTeamTagVisibility.ALWAYS)
    var color by observable(ChatColor.RESET)

    var displayName by observable("")
    var prefix by observable("")
    var suffix by observable("")

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendUpdates(ScoreboardTeamAction.CREATE, players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendUpdates(ScoreboardTeamAction.REMOVE, players)
    }

    fun sendUpdates(action: ScoreboardTeamAction, players: Collection<Player>) = PacketScoreboardTeam().also {
        require(action < ScoreboardTeamAction.ADD_ENTITIES) { "Modify entities not supported" }

        it.teamName = name
        it.action = action

        if (action != ScoreboardTeamAction.REMOVE) {
            it.displayName = displayName
            it.prefix = prefix
            it.suffix = suffix

            it.collision = collision
            it.visibility = visibility
            it.color = color
        }

        if (action == ScoreboardTeamAction.CREATE) {
            it.entities = entities
        }
    }.sendOrSendAll(players)

    fun sendUpdates(action: ScoreboardTeamAction, vararg players: Player) =
        sendUpdates(action, players.toList())

    protected final fun <V> observable(value: V): ReadWriteProperty<Any?, V> {
        val observer: (V, V) -> Unit =
            { _, _ -> if (hasViewers) sendUpdates(ScoreboardTeamAction.UPDATE) } //fuck kotlin with recursion bug!!!!
        return dev.zephyr.util.kotlin.observable(value, observer = observer)
    }

}