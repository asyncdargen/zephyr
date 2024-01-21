@file:Suppress("DEPRECATION")

package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamAction
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.util.collection.observe
import dev.zephyr.util.component.literal
import dev.zephyr.util.component.toComponent
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

    val entities = entities.toMutableList().observe(updateEntityAdd, updateEntityRemove)

    var collision by observable(ScoreboardTeamCollision.ALWAYS)
    var visibility by observable(ScoreboardTeamTagVisibility.ALWAYS)
    var color by observable(ChatColor.RESET)
    var flags by observable(0)

    var displayNameString: String
        set(value) {
            this.displayName = value.toComponent()
        }
        get() = this.displayName.literal()
    var prefixString: String
        set(value) {
            prefix = value.toComponent()
        }
        get() = prefix.literal()
    var suffixString: String
        set(value) {
            suffix = value.toComponent()
        }
        get() = suffix.literal()

    var displayName by observable<Component>(Component.empty())
    var prefix by observable<Component>(Component.empty())
    var suffix by observable<Component>(Component.empty())

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendUpdates(ScoreboardTeamAction.CREATE, players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        sendUpdates(ScoreboardTeamAction.REMOVE, players)
    }

    fun sendUpdates(action: ScoreboardTeamAction, players: Collection<Player>) = PacketScoreboardTeam().also {
        it.teamName = name
        it.action = action

        if (action != ScoreboardTeamAction.REMOVE && action < ScoreboardTeamAction.ADD_ENTITIES) {
            it.displayNameComponent = this.displayName
            it.prefixComponent = prefix
            it.suffixComponent = suffix

            it.collision = collision
            it.visibility = visibility
            it.color = color
            it.flags = flags
        }

        if (action == ScoreboardTeamAction.CREATE || action >= ScoreboardTeamAction.ADD_ENTITIES) {
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

private val ProtocolScoreboardTeam.updateEntityAdd: (String) -> Unit get() = { _ -> sendUpdates(ScoreboardTeamAction.ADD_ENTITIES) }
private val ProtocolScoreboardTeam.updateEntityRemove: (String) -> Unit get() = { _ -> sendUpdates(ScoreboardTeamAction.REMOVE_ENTITIES) }