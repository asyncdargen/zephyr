package dev.zephyr.menu.pagination

import dev.zephyr.menu.Menu
import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.icon.click
import dev.zephyr.menu.icon.icon
import dev.zephyr.menu.pagination.selector.PageSelector
import dev.zephyr.menu.pagination.selector.pageSelector
import dev.zephyr.menu.pagination.setting.PagedMenuSetting
import dev.zephyr.menu.pagination.supplier.PagePatternItemSupplier
import dev.zephyr.menu.pattern.pattern
import dev.zephyr.menu.setting.MenuSetting
import dev.zephyr.util.item.item
import dev.zephyr.util.kotlin.KotlinOpens
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * ' ' - items
 * '#' - empties
 * '<' - back page button
 * '>' - next page button
 * */
@KotlinOpens
class PagedMenu<T>(
    setting: MenuSetting,
    var title: Component,
    var itemMapper: (T) -> MenuIcon,
    var selector: PageSelector<T> = pageSelector(emptyList())
) : Menu(setting) {

    val pagedSetting = PagedMenuSetting()

    val pattern = pattern(
        "         ",
        "         ",
        "         ",
        "         ",
        "         ",
        "<#######>"
    ) {
        '<' {
            if (page == 0) icon(pagedSetting.emptyItem)
            else icon(pagedSetting.backPageItem) click { reopen(page - 1) }
        }
        '>' {
            if (page >= selector.getPages(pagedSetting.pageSize) - 1) icon(pagedSetting.emptyItem)
            else icon(pagedSetting.nextPageItem) click { reopen(page + 1) }
        }
        '#'(pagedSetting.emptyItem)
        ' '(PagePatternItemSupplier(this@PagedMenu))
    }

    var page = 0

    init {
        +pattern
    }

    fun open(player: Player, page: Int) {
        this.page = page
        open(player)
    }

    override fun open(player: Player) {
        this.page = page.coerceIn(0..<selector.getPages(pagedSetting.pageSize))
        setting.title = title.replaceText { it.matchLiteral("{page}").replacement("${page + 1}") }
        super.open(player)
    }

    fun reopen(page: Int) = open(viewer, page)

}

fun <T> pagedMenu(
    setting: MenuSetting,
    itemMapper: (T) -> MenuIcon = { icon(item(Material.AIR)) },
    selector: PageSelector<T> = pageSelector(emptyList()),
    block: PagedMenu<T>.() -> Unit
) = PagedMenu(setting, setting.title, itemMapper, selector).apply(block)

fun <T> pagedMenu(
    title: Component,
    itemMapper: (T) -> MenuIcon = { icon(item(Material.AIR)) },
    selector: PageSelector<T> = pageSelector(emptyList()),
    block: PagedMenu<T>.() -> Unit
) = pagedMenu(MenuSetting(title), itemMapper, selector, block)

