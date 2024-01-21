package dev.zephyr.menu.pagination.setting

import dev.zephyr.util.item.displayNameString
import dev.zephyr.util.item.item
import org.bukkit.Material

class PagedMenuSetting {

    var pageSize = 45
    var pageIndexMapper: (Int) -> Int = { it }

    var backPageItem = item(Material.ARROW) { displayNameString = "§aНазад" }
    var nextPageItem = item(Material.ARROW) { displayNameString = "§aВперед" }
    var emptyItem = item(Material.LIGHT_GRAY_STAINED_GLASS) { displayNameString = "" }

}