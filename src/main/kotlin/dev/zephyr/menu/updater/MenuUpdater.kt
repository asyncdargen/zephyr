package dev.zephyr.menu.updater

import dev.zephyr.menu.Menu

class MenuUpdater<M : Menu>(
    var delay: Int, val period: Int, var repeats: Int = -1,
    val action: M.() -> Unit
) {

    val isRunning get() = --repeats <= -1 || repeats > 0
    val isPendingTick: Boolean
        get() {
            val pending = delay-- <= 0
            if (pending) delay = period
            return pending
        }

    fun update(menu: M) = menu.action()

}

fun MutableCollection<MenuUpdater<Menu>>.update(menu: Menu): Boolean {
    var updated = false
    removeIf {
        if (it.isPendingTick) {
            updated = true
            it.update(menu)
        }

        !it.isRunning
    }
    return updated
}