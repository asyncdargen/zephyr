package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamAction
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.util.bukkit.toComponent
import dev.zephyr.util.kotlin.KotlinOpens
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*
import kotlin.properties.ReadWriteProperty

@KotlinOpens
class ProtocolScoreboardTeam(val name: String, entities: List<String>) : ProtocolObject() {
    constructor(uuid: UUID, entities: List<String>) : this(uuid.asId(), entities)
    constructor(name: String, vararg entities: String) : this(name, entities.toList())
    constructor(uuid: UUID, vararg entities: String) : this(uuid.asId(), entities.toList())
    constructor(name: String, entitiesUUIDs: Collection<UUID>) : this(name, entitiesUUIDs.map(UUID::toString))
    constructor(uuid: UUID, entitiesUUIDs: Collection<UUID>) : this(uuid.asId(), entitiesUUIDs.map(UUID::toString))
    constructor(name: String, vararg entitiesUUIDs: UUID) : this(name, entitiesUUIDs.map(UUID::toString))
    constructor(uuid: UUID, vararg entitiesUUIDs: UUID) : this(uuid.asId(), entitiesUUIDs.map(UUID::toString))

    companion object {
        protected fun UUID.asId() = toString().replace("-", "").substring(16)

    }

    val entities: MutableList<String> = entities.toMutableList() //todo: make observable list

    var collision by observable(ScoreboardTeamCollision.ALWAYS)
    var visibility by observable(ScoreboardTeamTagVisibility.ALWAYS)
    var color by observable(ChatColor.RESET)

    var displayName by observable("")
    var prefix by observable("")
    var suffix by observable("")

    var displayComponent: Component by observable(displayName.toComponent())
    var prefixComponent: Component by observable(prefix.toComponent())
    var suffixComponent: Component by observable(suffix.toComponent())

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
//            it.displayName = displayName
//            it.prefix = prefix
//            it.suffix = suffix

            it.displayComponent = displayComponent
            it.prefixComponent = prefixComponent
            it.suffixComponent = suffixComponent

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