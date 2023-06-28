package dev.zephyr.protocol.entity.data.villager

import com.comphenix.protocol.wrappers.AbstractWrapper
import dev.zephyr.protocol.entity.type.villager.VillagerProfession
import dev.zephyr.protocol.entity.type.villager.VillagerType
import dev.zephyr.util.kotlin.KotlinOpens

import net.minecraft.world.entity.npc.VillagerData as MinecraftVillagerData

@KotlinOpens
data class VillagerData(var type: VillagerType, var profession: VillagerProfession, var level: Int) :
    AbstractWrapper(MinecraftVillagerData::class.java) {

    companion object {

        val Default = VillagerData(VillagerType.PLAINS, VillagerProfession.NONE, 1)

    }

    override fun getHandle() = MinecraftVillagerData(type.handle, profession.handle, level)

}
