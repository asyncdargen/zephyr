package dev.zephyr.protocol.entity.mob.animal.villager

import com.comphenix.protocol.wrappers.WrappedVillagerData
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

val DefaultVillagerData = WrappedVillagerData.fromValues(
    WrappedVillagerData.Type.PLAINS,
    WrappedVillagerData.Profession.NONE, 1
)

@KotlinOpens
class ProtocolVillager(location: Location) : ProtocolAbstractVillager(EntityType.VILLAGER, location) {

    var villagerData by metadata.item(18, MetadataType.VillagerData, DefaultVillagerData)

}