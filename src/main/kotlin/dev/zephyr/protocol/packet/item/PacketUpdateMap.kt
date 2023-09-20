package dev.zephyr.protocol.packet.item

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import net.minecraft.world.level.saveddata.maps.WorldMap

@KotlinOpens
class PacketUpdateMap : ProtocolPacket(PacketPlayOutType.MAP) {

    var id by writer(0, integers)

    var data by writer<ByteArray> {
        modifier.write(4, WorldMap.b(128, 128, 0, 0, it))
    }

}