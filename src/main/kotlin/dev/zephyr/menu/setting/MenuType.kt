package dev.zephyr.menu.setting

import org.bukkit.event.inventory.InventoryType

enum class MenuType(val rows: Int, val columns: Int, val size: Int = rows * columns) {

    CHEST(3, 9),
    DISPENSER(3, 3),
    DROPPER(3, 3),
    FURNACE(1, 3),
    WORKBENCH(3, 4, 10),
    ENCHANTING(1, 2),
    BREWING(2, 3, 5),
    PLAYER(4, 9, 41),
    ENDER_CHEST(3, 9),
    ANVIL(1, 3),
    SMITHING(1, 3),
    BEACON(1, 1),
    HOPPER(1, 5),
    SHULKER_BOX(3, 9),
    BARREL(3, 9),
    BLAST_FURNACE(2, 2, 3),
    LECTERN(1, 1),
    SMOKER(2, 2, 3),
    LOOM(2, 2),
    CARTOGRAPHY(2, 2, 3),
    GRINDSTONE(2, 2, 3),
    STONECUTTER(1, 2),
    SMITHING_NEW(1, 4);

    val inventoryType by lazy { InventoryType.valueOf(name) }

}