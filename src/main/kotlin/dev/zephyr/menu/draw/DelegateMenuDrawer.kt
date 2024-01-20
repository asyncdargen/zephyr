package dev.zephyr.menu.draw

import dev.zephyr.menu.Menu
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
data class DelegateMenuDrawer<M : Menu>(val handler: M.() -> Unit) : MenuDrawer<M> {

    override fun draw(menu: M) = menu.handler()

}