package dev.zephyr.protocol.packet.player

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket

class PacketGameEvent : ProtocolPacket(PacketPlayOutType.GAME_STATE_CHANGE) {

    var type by writer(0, gameStateIDs)
    var value by writer(0, float)

}