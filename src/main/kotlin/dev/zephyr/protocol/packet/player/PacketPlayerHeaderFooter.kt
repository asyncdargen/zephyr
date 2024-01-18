package dev.zephyr.protocol.packet.player

import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class PacketPlayerHeaderFooter : ProtocolPacket(PacketPlayOutType.PLAYER_LIST_HEADER_FOOTER) {

    var headerString by writer(0, chatComponents, StringChatComponentMapper)
    var footerString by writer(1, chatComponents, StringChatComponentMapper)

    var header by writer(0, chatComponents, ChatComponentMapper)
    var footer by writer(1, chatComponents, ChatComponentMapper)

}