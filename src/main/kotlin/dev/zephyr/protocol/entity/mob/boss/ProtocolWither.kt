package dev.zephyr.protocol.entity.mob.boss

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolWither(location: Location) : ProtocolMonster(EntityType.WITHER, location) {

    var centerHeadTarget by metadata.item(16, MetadataType.Int, 0) //Center head's target (entity ID, or 0 if no target)
    var leftHeadTarget by metadata.item(17, MetadataType.Int, 0) //Left head's target (entity ID, or 0 if no target)
    var rightHeadTarget by metadata.item(18, MetadataType.Int, 0) //Right head's target (entity ID, or 0 if no target)
    var invulnerableTime by metadata.item(19, MetadataType.Int, 0) //Invulnerable time

}