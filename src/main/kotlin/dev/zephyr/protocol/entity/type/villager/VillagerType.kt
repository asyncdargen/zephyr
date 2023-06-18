package dev.zephyr.protocol.entity.type.villager

import dev.zephyr.extensions.java.isStatic
import dev.zephyr.extensions.java.tryAccessAndGet
import java.lang.reflect.Field
import net.minecraft.world.entity.npc.VillagerType as MinecraftVillagerType

enum class VillagerType {

    DESERT, JUNGLE, PLAINS, SAVANNA, SNOW, SWAMP, TAIGA;

    val handle: MinecraftVillagerType by lazy {
        MinecraftVillagerType::class.java.declaredFields
            .filter(Field::isStatic)[ordinal]
            .tryAccessAndGet()!!
    }

}