package dev.zephyr.protocol.entity.type.cat

import net.minecraft.core.registries.BuiltInRegistries

enum class CatVariant {

    TABBY,
    BLACK,
    RED,
    SIAMESE,
    BRITISH_SHORTHAIR,
    CALICO,
    PERSIAN,
    RAGDOLL,
    WHITE,
    JELLIE,
    ALL_BLACK;

    val handle by lazy { BuiltInRegistries.ai.a(ordinal) }

}