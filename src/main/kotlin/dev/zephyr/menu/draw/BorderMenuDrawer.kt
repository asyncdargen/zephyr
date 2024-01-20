package dev.zephyr.menu.draw

import dev.zephyr.menu.Menu
import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.icon.icon
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.inventory.ItemStack

@KotlinOpens
class BorderMenuDrawer(
    val icon: MenuIcon,
    val left: Boolean = false, val right: Boolean = false,
    val top: Boolean = false, val bottom: Boolean = false
) : MenuDrawer<Menu> {

    override fun draw(menu: Menu) {
        if (left || right) (0..<menu.setting.rows).forEach {
            if (left) menu[0, it] = icon
            if (right) menu[menu.setting.columns - 1, it] = icon
        }
        if (top || bottom) (0..<menu.setting.columns).forEach {
            if (top) menu[it, 0] = icon
            if (bottom) menu[it, menu.setting.rows - 1] = icon
        }
    }

}

fun borders(
    icon: MenuIcon,
    left: Boolean = false, right: Boolean = false,
    top: Boolean = false, bottom: Boolean = false
) = BorderMenuDrawer(icon, left, right, top, bottom)

fun borders(
    item: ItemStack,
    left: Boolean = false, right: Boolean = false,
    top: Boolean = false, bottom: Boolean = false
) = borders(icon(item), left, right, top, bottom)
