package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolGoat(location: Location) : ProtocolAnimal(EntityType.GOAT, location) {

    var isScreamingGoat by metadata.item(17, MetadataType.Boolean, false)
    var hasLeftHorn by metadata.item(18, MetadataType.Boolean, true)
    var hasRightHorn by metadata.item(19, MetadataType.Boolean, true)

}