package dev.zephyr.protocol.entity.mob.animal.llama

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.animal.horse.ProtocolChestedHorse
import dev.zephyr.protocol.entity.type.horse.LlamaVariant
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.DyeColor
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolLlama(location: Location, type: EntityType = EntityType.LLAMA) : ProtocolChestedHorse(type, location) {

    var strength by metadata.item(19, MetadataType.Int, 0) //Strength (number of columns of 3 slots in the llama's inventory once a chest is equipped)
    var carpetColor by metadata.item<DyeColor?, Int>(20, MetadataType.Int, null) { it?.ordinal ?: -1 }
    var variant by metadata.item(21, MetadataType.Int, LlamaVariant.CREAMY, LlamaVariant::ordinal)

}