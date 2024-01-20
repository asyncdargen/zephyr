package dev.zephyr.event

import dev.zephyr.util.function.ForkingContext
import org.bukkit.event.Event
import org.bukkit.event.EventPriority

typealias EventFilter = (Event) -> Boolean

interface EventContext : ForkingContext<EventContext> {

    val filters: MutableList<EventFilter>

    fun filter(filter: EventFilter): EventContext

    fun onListeners(vararg listeners: Any)

    fun <E : Event> on(
        type: Class<E>, priority: EventPriority = EventPriority.NORMAL,
        ignoreCancelled: Boolean = false, dependEvent: Boolean = false,
        handler: E.() -> Unit
    )

}

inline fun <reified E : Event> EventContext.filter(crossinline filter: (E) -> Boolean) =
    filter { if (it is E) filter(it) else true }

inline fun <reified E : Event> EventContext.on(
    priority: EventPriority = EventPriority.NORMAL,
    ignoreCancelled: Boolean = false, dependEvent: Boolean = false,
    noinline handler: E.() -> Unit
) = on(E::class.java, priority, ignoreCancelled, dependEvent, handler)