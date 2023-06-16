package dev.zephyr.event

import dev.zephyr.util.function.ForkingContext
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import java.util.function.Consumer

typealias EventFilter = (Event) -> Boolean

interface EventContext : ForkingContext<EventContext> {

    val filters: MutableList<EventFilter>
    fun filter(filter: EventFilter): EventContext

    fun onListeners(vararg listeners: Any)

    fun <E : Event> on(type: Class<E>, handler: Consumer<E>) =
        on(type, EventPriority.NORMAL, false, handler)

    fun <E : Event> on(type: Class<E>, handler: Consumer<E>, priority: EventPriority) =
        on(type, priority, false, handler)

    fun <E : Event> on(type: Class<E>, handler: Consumer<E>, ignoreCancelled: Boolean) =
        on(type, EventPriority.NORMAL, ignoreCancelled, handler)

    fun <E : Event> on(type: Class<E>, priority: EventPriority, ignoreCancelled: Boolean, handler: Consumer<E>)

}

inline fun <reified E : EventPriority> EventContext.filter(crossinline filter: (E) -> Boolean) =
    filter { if (it is E) filter(it) else true }

inline fun <reified E : Event> EventContext.on(
    noinline handler: E.() -> Unit
) = on(EventPriority.NORMAL, handler)

inline fun <reified E : Event> EventContext.on(
    ignoreCancelled: Boolean,
    noinline handler: E.() -> Unit
) = on(EventPriority.NORMAL, ignoreCancelled, handler)

inline fun <reified E : Event> EventContext.on(
    priority: EventPriority,
    noinline handler: E.() -> Unit
) = on(priority, false, handler)

inline fun <reified E : Event> EventContext.on(
    priority: EventPriority,
    ignoreCancelled: Boolean,
    noinline handler: E.() -> Unit
) = on(E::class.java, priority, ignoreCancelled, handler)

