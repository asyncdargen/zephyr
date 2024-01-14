package dev.zephyr.protocol.packet.player

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketPlayerHeaderFooter : ProtocolPacket(PacketPlayOutType.PLAYER_LIST_HEADER_FOOTER) {

    var header by writer(0, chatComponents, ChatComponentMapper)
    var footer by writer(1, chatComponents, ChatComponentMapper)

    var headerComponent by writer(0, chatComponents, KyoriComponentMapper)
    var footerComponent by writer(1, chatComponents, KyoriComponentMapper)

}