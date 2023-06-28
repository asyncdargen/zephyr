package dev.zephyr.protocol.entity.type.painting

import dev.zephyr.util.java.tryAccessAndGet
import net.minecraft.core.Holder
import net.minecraft.world.entity.decoration.PaintingVariant
import net.minecraft.world.entity.decoration.PaintingVariants

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

    val handle: HolderRecord<PaintingVariant> by lazy {
        HolderRecord(
            PaintingVariants::class.java.declaredFields[ordinal]
                .tryAccessAndGet()!!
        )
    }
}