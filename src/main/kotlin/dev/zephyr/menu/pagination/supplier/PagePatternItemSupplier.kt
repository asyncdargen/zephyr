package dev.zephyr.menu.pagination.supplier

import dev.zephyr.menu.icon.icon
import dev.zephyr.menu.pagination.PagedMenu
import dev.zephyr.menu.pattern.PatternIndex
import dev.zephyr.menu.pattern.supplier.PatternItemSupplier
import dev.zephyr.util.item.item
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Material

@KotlinOpens
class PagePatternItemSupplier<T>(val menu: PagedMenu<T>) : PatternItemSupplier {

    override fun supply(index: PatternIndex) = menu.run {
        selector.getPageItem(page, pagedSetting.pageSize, pagedSetting.pageIndexMapper(index.index))
            ?.let(itemMapper) ?: icon(item(Material.AIR))
    }

}