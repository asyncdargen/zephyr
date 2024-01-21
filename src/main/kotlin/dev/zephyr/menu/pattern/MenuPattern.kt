package dev.zephyr.menu.pattern

import dev.zephyr.menu.Menu
import dev.zephyr.menu.draw.MenuDrawer
import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.pattern.supplier.DelegatePatternItemSupplier
import dev.zephyr.menu.pattern.supplier.PatternItemSupplier
import dev.zephyr.menu.pattern.supplier.StaticPatternItemSupplier
import dev.zephyr.util.collection.concurrentHashMapOf
import org.bukkit.inventory.ItemStack

class MenuPattern(var pattern: String = "", var offsetY: Int = 0, var offsetX: Int = 0) : MenuDrawer<Menu> {

    val supplierMap = concurrentHashMapOf<Char, PatternItemSupplier>()

    override fun draw(menu: Menu) {
        var index = 0
        pattern.lineSequence().forEachIndexed { row, line ->
            line.forEachIndexed { column, char ->
                get(char)?.let {
                    menu[column + offsetX, row + offsetY] = it.supply(PatternIndex(row, column, index++))
                } ?: index++
            }
        }
    }


    operator fun get(char: Char) = supplierMap[char]

    fun remove(char: Char) = supplierMap.remove(char)


    operator fun set(char: Char, supplier: PatternItemSupplier) = supplierMap.put(char, supplier)

    operator fun set(char: Char, supplier: (PatternIndex) -> MenuIcon) =
        supplierMap.put(char, DelegatePatternItemSupplier(supplier))

    operator fun set(char: Char, icon: MenuIcon) = set(char, StaticPatternItemSupplier(icon))

    operator fun set(char: Char, item: ItemStack) = set(char, StaticPatternItemSupplier(item))


    operator fun Char.invoke(supplier: PatternItemSupplier) = set(this, supplier)

    operator fun Char.invoke(supplier: (PatternIndex) -> MenuIcon) = set(this, supplier)

    operator fun Char.invoke(icon: MenuIcon) = set(this, icon)

    operator fun Char.invoke(item: ItemStack) = set(this, item)


    fun pattern(vararg lines: String) {
        pattern = lines.joinToString("\n")
    }

}

fun pattern(vararg lines: String, offsetY: Int = 0, offsetX: Int = 0, block: MenuPattern.() -> Unit) =
    MenuPattern(lines.joinToString("\n"), offsetY, offsetX).apply(block)