package dev.zephyr.protocol.entity.type.painting

import net.minecraft.core.Holder
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.entity.decoration.PaintingVariants
import java.lang.reflect.Field

typealias HolderRecord<T> = Holder.a<T>

enum class ArtVariant {
    KEBAB,
    AZTEC,
    ALBAN,
    AZTEC2,
    BOMB,
    PLANT,
    WASTELAND,
    POOL,
    COURBET,
    SEA,
    SUNSET,
    CREEBET,
    WANDERER,
    GRAHAM,
    MATCH,
    BUST,
    STAGE,
    VOID,
    SKULL_AND_ROSES,
    WITHER,
    FIGHTERS,
    POINTER,
    PIGSCENE,
    BURNING_SKULL,
    SKELETON,
    DONKEY_KONG,
    EARTH,
    WIND,
    WATER,
    FIRE;

    val handle by lazy {
        HolderRecord(
            PaintingVariants::class.java.declaredFields[ordinal]
                .apply(Field::trySetAccessible)[null] as PaintingVariant
        )
    }
}