package dev.zephyr.menu.setting

import dev.zephyr.menu.setting.MenuType.CHEST
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit.createInventory
import org.bukkit.inventory.InventoryHolder

data class MenuSetting(
    var title: Component,
    var type: MenuType = CHEST,
    var size: Int = 54
) {
    constructor(title: Component, rows: Int) : this(title, CHEST, rows * 9)
    constructor(title: Component, type: MenuType) : this(title, type, type.size)

    var editable = false

    var rows
        get() = if (type === CHEST) size / 9 else type.rows
        set(value) {
            size = value * 9
            type = CHEST
        }
    val columns get() = type.columns

    fun createInventory(holder: InventoryHolder) =
        if (type == CHEST) createInventory(holder, size, title)
        else createInventory(holder, type.inventoryType, title)


}