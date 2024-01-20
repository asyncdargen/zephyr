package dev.zephyr.menu.pagination.selector

import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
data class StaticPageSelector<T>(override var items: List<T>) : PageSelector<T>

fun <T> pageSelector(collection: List<T>) = StaticPageSelector(collection)