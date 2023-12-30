package dev.zephyr.protocol.entity.world.display

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.util.BoundingBox

@KotlinOpens
class ProtocolInteraction(location: Location) : ProtocolEntity(EntityType.INTERACTION, location) {

    var width by metadata.item(8, MetadataType.Float, 1f)
    var height by metadata.item(9, MetadataType.Float, 1f)
    var responsive by metadata.item(10, MetadataType.Boolean, true).defaults()

    override val boundingBox
        get() = BoundingBox(
            .0, .0, .0,
            width.toDouble(), height.toDouble(), width.toDouble()
        )

}