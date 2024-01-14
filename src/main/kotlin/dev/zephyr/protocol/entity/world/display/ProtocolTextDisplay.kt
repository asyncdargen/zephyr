package dev.zephyr.protocol.entity.world.display

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.display.TextDisplayAlignment
import dev.zephyr.protocol.packet.ProtocolPacket.Companion.StringChatComponentMapper
import dev.zephyr.protocol.packet.ProtocolPacket.Companion.ChatComponentMapper
import dev.zephyr.util.bukkit.toComponent
import dev.zephyr.util.kotlin.KotlinOpens
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolTextDisplay(location: Location) : ProtocolDisplay(location, EntityType.TEXT_DISPLAY) {

    var text by metadata.item(22, MetadataType.Chat, "", StringChatComponentMapper)
    var textComponent by metadata.item(22, MetadataType.Chat, MiniMessage.miniMessage().deserialize(""), ChatComponentMapper)

    var lineWidth by metadata.item(23, MetadataType.Int, 200)
    var backgroundColor by metadata.item(24, MetadataType.Int, 0x40000000)
    var textOpacity by metadata.item(25, MetadataType.Byte, -1)

    var isShadowed by metadata.bitMask(26, 0x01)
    var isSeeThrough by metadata.bitMask(26, 0x02)
    var isDefaultBackground by metadata.bitMask(26, 0x04)
    var textAlignment by metadata.bitMask(26, TextDisplayAlignment.CENTER, TextDisplayAlignment::id)

    fun removeBackground() {
        backgroundColor = 0
    }

}
