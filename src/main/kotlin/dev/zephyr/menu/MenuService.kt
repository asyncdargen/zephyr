package dev.zephyr.menu

import dev.zephyr.util.bukkit.every
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.safeCast
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryCloseEvent.Reason

object MenuService {

    val cached = concurrentSetOf<Menu>()

    init {
        on<InventoryCloseEvent> {
            val holder = inventory.holder?.safeCast<Menu>() ?: return@on
            if (reason != Reason.OPEN_NEW || player.openInventory.topInventory.holder !== holder) {
                holder.close(true)
            }
        }
        every(1, 1) { cached.forEach { it.tick() } }
    }

    fun add(menu: Menu) = cached.add(menu)

    fun remove(menu: Menu) = cached.remove(menu)

}