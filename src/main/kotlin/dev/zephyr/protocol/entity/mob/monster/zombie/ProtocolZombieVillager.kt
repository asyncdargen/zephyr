package dev.zephyr.protocol.entity.mob.monster.zombie

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.mob.animal.villager.DefaultVillagerData
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolZombieVillager(location: Location) : ProtocolZombie(EntityType.ZOMBIE_VILLAGER, location) {

    var isConverting by metadata.item(19, MetadataType.Boolean, false)
    var villagerData by metadata.item(20, MetadataType.VillagerData, DefaultVillagerData)

}