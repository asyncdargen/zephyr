package dev.zephyr.protocol.packet.player

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketPlayerInfoUpdate : ProtocolPacket(PacketPlayOutType.PLAYER_INFO) {

    var actions by writer(0, playerInfoActions)

    var playerInfos by writer(1, playerInfoDataLists)

}