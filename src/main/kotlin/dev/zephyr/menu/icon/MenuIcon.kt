package dev.zephyr.menu.icon

import dev.zephyr.menu.Menu
import dev.zephyr.menu.updater.MenuUpdater
import dev.zephyr.menu.updater.update
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.cast
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

typealias MenuIconAction = Menu.(InventoryClickEvent) -> Unit

@KotlinOpens
class MenuIcon(
    var itemStack: ItemStack,
    var defaultSlot: Int,
    var action: MenuIconAction = {}
) {

    var dirty = false
    val updaters: MutableSet<MenuUpdater<Menu>> = concurrentSetOf()

    fun dirty() {
        dirty = true
    }

    fun onUpdate(menu: Menu) {
        if (updaters.update(menu)) dirty()
    }

    fun onClick(menu: Menu, event: InventoryClickEvent) {
        menu.action(event)
    }

}

fun icon(
    item: ItemStack, slot: Int = 0,
    block: MenuIcon.() -> Unit = { }
) = MenuIcon(item.clone(), slot).apply(block)

fun ItemStack.asIcon(slot: Int = 0, block: MenuIcon.() -> Unit = { }) = icon(this, slot, block)

infix fun <I : MenuIcon> I.click(action: MenuIconAction) = apply { this.action = action.cast() }

fun <I : MenuIcon> I.updater(
    delay: Int = 20,
    period: Int = 20,
    repeats: Int = -1,
    block: Menu.(I) -> Unit
) = apply { updaters.add(MenuUpdater(delay, period, repeats) { block(this@apply) }) }


