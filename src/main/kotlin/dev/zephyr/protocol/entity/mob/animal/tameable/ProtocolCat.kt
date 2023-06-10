package dev.zephyr.protocol.entity.mob.animal.tameable

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.cat.CatVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolCat(location: Location) : ProtocolTameableAnimal(EntityType.CAT, location) {

    var variant by metadata.item(19, MetadataType.CatVariant, CatVariant.BLACK)
    var isLying by metadata.item(20, MetadataType.Boolean, false)
    var isRelaxed by metadata.item(21, MetadataType.Boolean, false)
    var collarColor by metadata.item(20, MetadataType.Int, DyeColor.RED, DyeColor::ordinal)

}