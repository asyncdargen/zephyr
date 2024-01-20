package dev.zephyr.menu.pagination.selector

class LazyPageSelector<T>(val itemsSupplier: () -> List<T>) : PageSelector<T> {

    override val items get() = itemsSupplier()

    override fun getPage(page: Int, size: Int): Iterable<T> = items.slice((page * size)..(page * size + page))

}

fun <T> pageSelector(itemsSupplier: () -> List<T>) = LazyPageSelector(itemsSupplier)