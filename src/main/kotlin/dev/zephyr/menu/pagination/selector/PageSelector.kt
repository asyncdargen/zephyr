package dev.zephyr.menu.pagination.selector

import kotlin.math.absoluteValue

interface PageSelector<T> {

    val items: List<T>
    val size: Int get() = items.size

    fun getPages(pageSize: Int): Int = size / pageSize + (size % pageSize).absoluteValue.coerceIn(0..1)

    fun getPage(page: Int, size: Int): Iterable<T> = items.slice((size * page)..(size * page + page).coerceAtMost(size))

    fun getPageItem(page: Int, size: Int, index: Int) = items.getOrNull(page * size + index)

}