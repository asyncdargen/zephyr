package dev.zephyr.util.bukkit

import dev.zephyr.event.GlobalEventContext
import dev.zephyr.event.on
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

inline fun <reified E : Event> on(
    noinline handler: E.() -> Unit
) = GlobalEventContext.on(handler)

inline fun <reified E : Event> on(
    ignoreCancelled: Boolean,
    noinline handler: E.() -> Unit
) = GlobalEventContext.on(ignoreCancelled, handler)

inline fun <reified E : Event> on(
    priority: EventPriority,
    noinline handler: E.() -> Unit
) = GlobalEventContext.on(priority, handler)

inline fun <reified E : Event> on(
    priority: EventPriority,
    ignoreCancelled: Boolean,
    noinline handler: E.() -> Unit
) = GlobalEventContext.on(priority, ignoreCancelled, handler)

fun <E : Event> postEvent(event: E) = event.apply(PluginManager::callEvent)

fun <E : Event> E.call() = postEvent(this)

fun <E> E.cancel() where E : Cancellable {
    if (!isCancelled) isCancelled = true
}