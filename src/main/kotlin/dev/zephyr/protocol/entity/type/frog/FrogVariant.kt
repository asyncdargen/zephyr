package dev.zephyr.protocol.entity.type.frog

import net.minecraft.core.registries.BuiltInRegistries
import org.bukkit.NamespacedKey
import org.bukkit.craftbukkit.v1_19_R3.util.CraftNamespacedKey

enum class FrogVariant {

    TEMPERATE,
    WARM,
    COLD;

    val handle by lazy { BuiltInRegistries.aj.a(CraftNamespacedKey.toMinecraft(NamespacedKey.minecraft(name))) }

}