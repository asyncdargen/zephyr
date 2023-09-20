package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolBee(location: Location) : ProtocolAnimal(EntityType.BEE, location) {

    var isAngry by metadata.bitMask(17, 0x02)
    var hasStung by metadata.bitMask(17, 0x04, true)
    var hasNectar by metadata.bitMask(17, 0x08)
    var angerTime by metadata.item(18, MetadataType.Int, 0)

}