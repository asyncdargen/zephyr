package dev.zephyr.protocol.entity.type.villager

import dev.zephyr.extensions.java.isStatic
import dev.zephyr.extensions.java.tryAccessAndGet
import java.lang.reflect.Field
import net.minecraft.world.entity.npc.VillagerProfession as MinecraftVillagerProfession

enum class VillagerProfession {

    NONE, ARMORER, BUTCHER, CARTOGRAPHER, CLERIC, FARMER, FISHERMAN,
    FLETCHER, LEATHERWORKER, LIBRARIAN, MASON, NITWIT, SHEPHERD,
    TOOLSMITH, WEAPONSMITH;

    val handle: MinecraftVillagerProfession by lazy {
        MinecraftVillagerProfession::class.java.declaredFields
            .filter(Field::isStatic)[ordinal + 1]
            .tryAccessAndGet()!!
    }

}