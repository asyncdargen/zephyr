package dev.zephyr.extensions

import dev.zephyr.Zephyr
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority


val PluginManager = Bukkit.getPluginManager()

inline fun <reified E : Event> on(
    priority: EventPriority, ignoreCancelled: Boolean,
    crossinline block: E.() -> Unit
) = PluginManager.registerEvent(
    E::class.java, Zephyr, EventPriority.NORMAL,
    { _, event -> block(event as E) }, Zephyr.Plugin
)

inline fun <reified E : Event> on(
    ignoreCancelled: Boolean,
    crossinline block: E.() -> Unit
) = on(EventPriority.NORMAL, ignoreCancelled, block)

inline fun <reified E : Event> on(
    priority: EventPriority,
    crossinline block: E.() -> Unit
) = on(priority, false, block)

inline fun <reified E : Event> on(
    crossinline block: E.() -> Unit
) = on(EventPriority.NORMAL, false, block)
