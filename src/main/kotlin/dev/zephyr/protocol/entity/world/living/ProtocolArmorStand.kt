package dev.zephyr.protocol.entity.world.living

import com.comphenix.protocol.wrappers.Vector3F
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.ProtocolLivingEntity
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolArmorStand(location: Location) : ProtocolLivingEntity(EntityType.ARMOR_STAND, location) {

    var isSmall by metadata.bitMask(15, 0x01)
    var hasArms by metadata.bitMask(15, 0x02)
    var noBasePlate by metadata.bitMask(15, 0x08)
    var isMarker by metadata.bitMask(15, 0x10)

    var headRotation by metadata.item(16, MetadataType.Vector, Vector3F(0f, 0f ,0f))
    var bodyRotation by metadata.item(17, MetadataType.Vector, Vector3F(0f, 0f ,0f))
    var leftArmRotation by metadata.item(18, MetadataType.Vector, Vector3F(-10f, 0f ,-10f))
    var rightArmRotation by metadata.item(19, MetadataType.Vector, Vector3F(-15f, 0f ,10f))
    var leftLegRotation by metadata.item(20, MetadataType.Vector, Vector3F(-1f, 0f ,-1f))
    var rightLegRotation by metadata.item(21, MetadataType.Vector, Vector3F(1f, 0f ,1f))

}