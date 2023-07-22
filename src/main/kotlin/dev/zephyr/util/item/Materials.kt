package dev.zephyr.util.item

import org.bukkit.Material

val Material.isLiquid
    get() = this === Material.WATER || this === Material.LAVA