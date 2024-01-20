package dev.zephyr.menu.pattern.supplier

import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.icon.icon
import dev.zephyr.menu.pattern.PatternIndex
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.inventory.ItemStack

@KotlinOpens
data class StaticPatternItemSupplier(val icon: MenuIcon) : PatternItemSupplier {
    constructor(item: ItemStack) : this(icon(item))

    override fun supply(index: PatternIndex) = icon


}