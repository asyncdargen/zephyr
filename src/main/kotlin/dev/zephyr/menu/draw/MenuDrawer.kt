package dev.zephyr.menu.draw

import dev.zephyr.menu.Menu

interface MenuDrawer<M : Menu> {

    fun draw(menu: M)

}