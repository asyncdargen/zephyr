package dev.zephyr.event

import org.bukkit.event.HandlerList

abstract class HandlerListHolder {

    @get:JvmName("_getHandlerList")
    protected val handlerList = HandlerList()

}