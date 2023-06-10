package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolPanda(location: Location) : ProtocolAnimal(EntityType.PANDA, location) {

    var breedTimer by metadata.item(17, MetadataType.Int, 0)
    var sneezeTimer by metadata.item(18, MetadataType.Int, 0)
    var eatTimer by metadata.item(19, MetadataType.Int, 0)
    var mainGene by metadata.item(20, MetadataType.Byte, 0)
    var hiddenGene by metadata.item(21, MetadataType.Byte, 0)

    var isSneezing by metadata.bitMask(22, 0x02)
    var isRolling by metadata.bitMask(22, 0x04)
    var isSitting by metadata.bitMask(22, 0x08)
    var isOnBack by metadata.bitMask(22, 0x10)
}