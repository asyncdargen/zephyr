package dev.zephyr.menu.pattern.supplier

import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.pattern.PatternIndex
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
data class DelegatePatternItemSupplier(val supplier: (PatternIndex) -> MenuIcon) : PatternItemSupplier {

    override fun supply(index: PatternIndex): MenuIcon = supplier(index)

}