package dev.zephyr.protocol.entity.mob.monster

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

@KotlinOpens
class ProtocolEnderman(location: Location) : ProtocolMonster(EntityType.ENDERMAN, location) {

    var carriedBlock by metadata.item(16, MetadataType.BlockDataOptional, Optional.empty())
    var isScreaming by metadata.item(17, MetadataType.Boolean, false)
    var isStaring by metadata.item(18, MetadataType.Boolean, false)

}