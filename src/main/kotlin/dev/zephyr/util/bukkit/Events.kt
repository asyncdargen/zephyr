package dev.zephyr.util.bukkit

import dev.zephyr.event.GlobalEventContext
import dev.zephyr.event.on
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

inline fun <reified E : Event> on(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false,
    dependEvent: Boolean = false,
    noinline handler: E.() -> Unit
) = GlobalEventContext.on(priority, ignoreCancelled, dependEvent, handler)

fun <E : Event> postEvent(event: E) = event.apply(PluginManager::callEvent)

fun <E : Event> E.call() = postEvent(this)

fun <E> E.cancel() where E : Cancellable {
    if (!isCancelled) isCancelled = true
}