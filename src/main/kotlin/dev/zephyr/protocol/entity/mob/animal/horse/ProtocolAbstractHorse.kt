package dev.zephyr.protocol.entity.mob.animal.horse

import dev.zephyr.protocol.entity.mob.animal.ProtocolAnimal
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAbstractHorse(type: EntityType, location: Location) : ProtocolAnimal(type, location) {

    var isTame by metadata.bitMask(17, 0x02)
    var isSaddled by metadata.bitMask(17, 0x04)
    var hasBred by metadata.bitMask(17, 0x08)
    var isEating by metadata.bitMask(17, 0x10)
    var isRearing by metadata.bitMask(17, 0x20)
    var isMouthOpen by metadata.bitMask(17, 0x40)

}