package dev.zephyr.protocol.packet.scoreboard

import com.comphenix.protocol.utility.MinecraftReflection.getMinecraftClass
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamAction
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.ChatColor

@KotlinOpens
class PacketScoreboardTeam : ProtocolPacket(PacketPlayOutType.SCOREBOARD_TEAM) {

    var teamName by writer(0, strings)
    var action by writer<ScoreboardTeamAction, Int>(0, integers) { it.ordinal }
    var entities by writer(0, getSpecificModifier(Collection::class.java))

    val structure by lazy { optionalStructures.read(0).get() }

    var displayName by writer(0, structure.chatComponents, StringChatComponentMapper)
    var prefix by writer(1, structure.chatComponents, StringChatComponentMapper)
    var suffix by writer(2, structure.chatComponents, StringChatComponentMapper)

    var displayNameComponent by writer(0, structure.chatComponents, ChatComponentMapper)
    var prefixComponent by writer(1, structure.chatComponents, ChatComponentMapper)
    var suffixComponent by writer(2, structure.chatComponents, ChatComponentMapper)

    var visibility by writer(0, structure.strings, ScoreboardTeamTagVisibility::handleName)
    var collision by writer(1, structure.strings, ScoreboardTeamCollision::handleName)
    var flags by writer(0, structure.integers)

    var color by writer(0, structure.getEnumModifier(ChatColor::class.java, getMinecraftClass("EnumChatFormat")))

}