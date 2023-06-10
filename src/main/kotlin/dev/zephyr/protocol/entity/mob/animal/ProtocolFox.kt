package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.fox.FoxVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType
import java.util.*

@KotlinOpens
class ProtocolFox(location: Location) : ProtocolAnimal(EntityType.FOX, location) {

    var variant by metadata.item(17, MetadataType.Int, FoxVariant.RED, FoxVariant::ordinal)
    var isSitting by metadata.bitMask(18, 0x01)
    var isCrouching by metadata.bitMask(18, 0x04)
    var isInterested by metadata.bitMask(18, 0x08)
    var isPouncing by metadata.bitMask(18, 0x10)
    var isSleeping by metadata.bitMask(18, 0x20)
    var isFaceplanted by metadata.bitMask(18, 0x40)
    var isDefending by metadata.bitMask(18, 0x80)
    var firstUUID by metadata.item(19, MetadataType.UUIDOptional, Optional.empty()) //First UUID (in UUIDs NBT)?
    var secondUUID by metadata.item(20, MetadataType.UUIDOptional, Optional.empty()) //Second UUID (in UUIDs NBT)?

}