package dev.zephyr.util.bukkit

import net.minecraft.world.entity.EntityTypes
import org.bukkit.entity.EntityType
import org.bukkit.util.BoundingBox

val EntityType.handle
    get() = EntityTypes.a(key.key).get()

val EntityType.boundingBox
    get() = handle.n().a(.0, .0, .0).run { BoundingBox(a, b, c, d, e, f) }