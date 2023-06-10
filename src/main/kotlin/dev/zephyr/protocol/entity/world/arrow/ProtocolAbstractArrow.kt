package dev.zephyr.protocol.entity.world.arrow

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractArrow(type: EntityType, location: Location) : ProtocolEntity(type, location) {

    var isCritical by metadata.bitMask(8, 0x01)
    var isNoClip by metadata.bitMask(8, 0x02)

    var piercingLevel by metadata.item(9, MetadataType.Byte, 0)

}