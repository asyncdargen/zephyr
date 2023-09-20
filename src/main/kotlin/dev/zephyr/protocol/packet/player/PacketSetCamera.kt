package dev.zephyr.protocol.packet.player

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket

class PacketSetCamera : ProtocolPacket(PacketPlayOutType.CAMERA) {

    var cameraId by writer(0, integers)

}