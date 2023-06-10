package dev.zephyr.protocol.packet.player

import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketPlayerInfoRemove : ProtocolPacket(PacketPlayOutType.PLAYER_INFO_REMOVE) {

    var playersUUIDs by writer(0, uuidLists)

}