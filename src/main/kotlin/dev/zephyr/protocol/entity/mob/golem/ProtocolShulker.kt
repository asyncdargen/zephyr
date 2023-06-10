package dev.zephyr.protocol.entity.mob.golem

import com.comphenix.protocol.wrappers.EnumWrappers.Direction
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

@KotlinOpens
class ProtocolShulker(location: Location) : ProtocolAbstractGolem(EntityType.SHULKER, location) {

    var attachFace by metadata.item(16, MetadataType.Direction, Direction.DOWN)
    var attachmentPosition by metadata.item(17, MetadataType.BlockPositionOptional, Optional.empty())
    var shieldHeight by metadata.item(18, MetadataType.Byte, 0)
    var color by metadata.item<DyeColor?, Byte>(19, MetadataType.Byte, null) { (it?.ordinal ?: -1).toByte() }

}