package dev.zephyr.protocol.entity.mob.animal.villager

import dev.zephyr.protocol.entity.data.villager.VillagerData
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolVillager(location: Location) : ProtocolAbstractVillager(EntityType.VILLAGER, location) {

    var villagerData by metadata.item(18, MetadataType.VillagerData, VillagerData.Default)

}