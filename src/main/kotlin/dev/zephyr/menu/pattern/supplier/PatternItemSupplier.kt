package dev.zephyr.menu.pattern.supplier

import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.pattern.PatternIndex

interface PatternItemSupplier {

    fun supply(index: PatternIndex): MenuIcon

}