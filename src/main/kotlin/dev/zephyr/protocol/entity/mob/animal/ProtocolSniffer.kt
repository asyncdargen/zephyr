package dev.zephyr.protocol.entity.mob.animal

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.sniffer.SnifferState
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSniffer(location: Location) : ProtocolAnimal(EntityType.SNIFFER, location) {

    var snifferState by metadata.item(17, MetadataType.SnifferState, SnifferState.IDLING)
    var dropSeedAtTick by metadata.item(18, MetadataType.Int, 0)

}