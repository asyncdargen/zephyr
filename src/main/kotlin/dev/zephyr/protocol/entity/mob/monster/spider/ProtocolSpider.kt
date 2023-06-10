package dev.zephyr.protocol.entity.mob.monster.spider

import dev.zephyr.protocol.entity.mob.monster.ProtocolMonster
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSpider(location: Location, type: EntityType = EntityType.SPIDER) : ProtocolMonster(type, location) {

    var isClimbing by metadata.bitMask(16, 0x01)

}