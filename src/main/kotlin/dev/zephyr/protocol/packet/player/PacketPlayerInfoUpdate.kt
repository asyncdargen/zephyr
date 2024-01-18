package dev.zephyr.protocol.packet.player

import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction
import com.comphenix.protocol.wrappers.PlayerInfoData
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketPlayerInfoUpdate : ProtocolPacket(PacketPlayOutType.PLAYER_INFO) {

    var actions by writer(0, playerInfoActions)

    var playerInfos by writer(1, playerInfoDataLists)

    companion object {

        fun single(data: PlayerInfoData, vararg actions: PlayerInfoAction) = PacketPlayerInfoUpdate().also {
            it.playerInfos = listOf(data)
            it.actions = actions.toSet()
        }

        fun many(data: List<PlayerInfoData>, vararg actions: PlayerInfoAction)= PacketPlayerInfoUpdate().also {
            it.playerInfos = data
            it.actions = actions.toSet()
        }

    }

}