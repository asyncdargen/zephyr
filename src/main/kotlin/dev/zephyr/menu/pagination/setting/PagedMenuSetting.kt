package dev.zephyr.menu.pagination.setting

import dev.zephyr.util.item.displayName
import dev.zephyr.util.item.item
import org.bukkit.Material

class PagedMenuSetting {

    var pageSize = 45
    var pageIndexMapper: (Int) -> Int = { it }

    var backPageItem = item(Material.ARROW) { displayName = "§aНазад" }
    var nextPageItem = item(Material.ARROW) { displayName = "§aВперед" }
    var emptyItem = item(Material.LIGHT_GRAY_STAINED_GLASS) { displayName = "" }

}