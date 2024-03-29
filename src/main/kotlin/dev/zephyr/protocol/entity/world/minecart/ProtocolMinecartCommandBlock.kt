package dev.zephyr.protocol.entity.world.minecart

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.packet.ProtocolPacket.Companion.StringChatComponentMapper
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolMinecartCommandBlock(location: Location) : ProtocolAbstractMinecart(EntityType.MINECART_COMMAND, location) {

    var command by metadata.item(14, MetadataType.String, "")
    var lastOutput by metadata.item(15, MetadataType.Chat, "", StringChatComponentMapper)

}