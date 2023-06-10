package dev.zephyr.protocol.entity.mob.animal.horse

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.horse.HorseColor
import dev.zephyr.protocol.entity.type.horse.HorseStyle
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolHorse(type: EntityType, location: Location) : ProtocolAbstractHorse(type, location) {

    var variant by metadata.item(18, MetadataType.Int, 0) //Variant (Color & Style)

    var color by observable(HorseColor.BLACK) { _, _ -> computeVariant() }
    var style by observable(HorseStyle.NONE) { _, _ -> computeVariant() }

    fun computeVariant() {
        variant = (color.ordinal and 0xFF) or (style.ordinal shl 8 and 0xFF00)
    }

}