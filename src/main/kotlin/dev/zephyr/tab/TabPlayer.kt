package dev.zephyr.tab

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedGameProfile
import dev.zephyr.hook.vehicle.mount
import dev.zephyr.protocol.entity.access
import dev.zephyr.protocol.entity.register
import dev.zephyr.protocol.entity.type.display.DisplayBillBoard
import dev.zephyr.protocol.entity.world.display.ProtocolTextDisplay
import dev.zephyr.protocol.entity.world.display.removeShadow
import dev.zephyr.protocol.packet.player.PacketPlayerHeaderFooter
import dev.zephyr.protocol.packet.player.PacketPlayerInfoUpdate
import dev.zephyr.protocol.scoreboard.ProtocolScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.tab.TabPlayer.Properties.tagAccessor
import dev.zephyr.tab.TabPlayer.Properties.tagFactory
import dev.zephyr.tab.TabPlayer.Properties.tagSelfAccess
import dev.zephyr.tab.TabPlayer.Properties.teamFactory
import dev.zephyr.util.bukkit.clearAngles
import dev.zephyr.util.bukkit.isTracked
import dev.zephyr.util.component.components
import dev.zephyr.util.component.literal
import dev.zephyr.util.component.toComponent
import dev.zephyr.util.component.wrap
import dev.zephyr.util.kotlin.observable
import dev.zephyr.util.numbers.length
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.joml.Vector3f

class TabPlayer(val player: Player) {

    var container = ProtocolContainer()

    var dirtyHeaderFooter = true
    var dirtyTeam = true
    var dirtyName = true

    var order by observable(0, true) { _, _ -> dirtyTeam = true }

    var displayName by observable<Component>(player.name.toComponent(), true) { _, _ -> dirtyName = true }
    var header by observable<Component>(Component.empty(), true) { _, _ -> dirtyHeaderFooter = true }
    var footer by observable<Component>(Component.empty(), true) { _, _ -> dirtyHeaderFooter = true }
    var tagName: Component
        get() = container.tag.text
        set(value) {
            if (container.tag.text != value) {
                container.tag.text = value
            }
        }

    var displayNameString: String
        get() = displayName.literal()
        set(value) {
            displayName = value.toComponent()
        }
    var headerString: String
        get() = header.literal()
        set(value) {
            header = value.toComponent()
        }
    var footerString: String
        get() = footer.literal()
        set(value) {
            footer = value.toComponent()
        }
    var tagNameString: String
        get() = tagName.literal()
        set(value) {
            tagName = value.toComponent()
        }

    fun tagLines(vararg lines: Component) {
        tagName = components(*lines)
    }

    fun tagStringLines(vararg lines: String) {
        tagName = components(*lines)
    }

    fun update(force: Boolean = false) {
        if (!container.tag.isRegistered()) with(container.tag) {
            access {
                (tagSelfAccess || player !== it) && it.isTracked(player)
                        && tagAccessor(player, it) && it.canSee(player) && !player.isDead
                        && (player.gameMode == GameMode.SPECTATOR && player.gameMode == it.gameMode || player.gameMode != GameMode.SPECTATOR)
            }
            register()
            player.mount(this)
        }

        if (dirtyHeaderFooter) PacketPlayerHeaderFooter().also {
            dirtyHeaderFooter = false
            it.header = header
            it.footer = footer
        }.send(player)

        if (dirtyTeam) {
            dirtyTeam = false
            container.recreateTeam().broadcastSpawn()
        }

        if (dirtyName) {
            container.removeProfile()
            if (force) {
                dirtyName = false
                PacketPlayerInfoUpdate.single(
                    container.getOrCreateProfile(),
                    PlayerInfoAction.UPDATE_DISPLAY_NAME
                ).broadcast()
            }
        }
    }

    fun force(block: TabPlayer.() -> Unit = {}) {
        block(this)
        update(true)
    }

    fun remove() {
        container.tag.remove()
        container.removeTeam()
        container.removeProfile()
    }

    inner class ProtocolContainer {

        private var team: ProtocolScoreboardTeam? = null
        private val teamName get() = order.orderTeamName

        private var profile: PlayerInfoData? = null

        var tag = tagFactory(player)

        fun resneakTag(sneaking: Boolean = player.isSneaking) = tag.apply {
            if (sneaking) {
                isSeeThrough = false
                textOpacity = 63
            } else {
                isSeeThrough = true
                textOpacity = -1
            }
        }

        fun getOrCreateTeam() = team ?: teamFactory(player, teamName).also { team = it }

        fun removeTeam() {
            team?.remove()
            team = null
        }

        fun recreateTeam(): ProtocolScoreboardTeam {
            removeTeam()
            return getOrCreateTeam()
        }

        fun getOrCreateProfile() = profile ?: PlayerInfoData(
            WrappedGameProfile.fromPlayer(player), player.ping,
            EnumWrappers.NativeGameMode.fromBukkit(player.gameMode),
            displayName.wrap()
        )

        fun removeProfile() {
            profile = null
        }

        fun recreateProfile(): PlayerInfoData {
            removeProfile()
            return getOrCreateProfile()
        }

    }

    object Properties {

        var teamFactory: (Player, String) -> ProtocolScoreboardTeam = { player, name ->
            ProtocolScoreboardTeam(name, player.name).apply {
                collision = ScoreboardTeamCollision.NEVER
                visibility = ScoreboardTeamTagVisibility.NEVER
            }
        }
        var tagFactory: (Player) -> ProtocolTextDisplay = {
            ProtocolTextDisplay(it.location.clearAngles()).apply {
                isSeeThrough = true
                isShadowed = true
                text = it.name.toComponent()
                translation = Vector3f(0f, .6f, 0f)
                billboard = DisplayBillBoard.VERTICAL
                removeBackground()
                removeShadow()
            }
        }
        var tagAccessor: (Player, Player) -> Boolean = { _, _ -> true }

        var tagSelfAccess = true
        var maxOrder = 1000

    }

    companion object {

        private val Int.orderTeamName
            get() = (Properties.maxOrder - this).run { "${"0".repeat(Properties.maxOrder.length - toString().length)}$this" }

    }

}