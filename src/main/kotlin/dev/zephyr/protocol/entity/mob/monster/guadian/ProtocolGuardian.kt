package dev.zephyr.protocol.entity.mob.monster.guadian

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolGuardian(location: Location, type: EntityType = EntityType.GUARDIAN) : ProtocolMonster(type, location) {

    var isRetractingSpikes by metadata.item(16, MetadataType.Boolean, false)
    var targetEntityId by metadata.item(17, MetadataType.Int, 0)

}