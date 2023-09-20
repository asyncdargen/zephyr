package dev.zephyr.protocol.packet.world

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketBlockAck(sequence: Int) : ProtocolPacket(PacketPlayOutType.BLOCK_CHANGED_ACK) {

    var sequence by writer(0, integers)

    init {
        this.sequence = sequence
    }

}