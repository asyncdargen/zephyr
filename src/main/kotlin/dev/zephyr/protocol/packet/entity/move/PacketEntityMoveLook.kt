package dev.zephyr.protocol.packet.entity.move

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketEntityMoveLook : PacketEntityMove(PacketPlayOutType.REL_ENTITY_MOVE_LOOK) {

    var yaw by writer(0, bytes, AngleMapper)
    var pitch by writer(0, bytes, AngleMapper)

}