package dev.zephyr.event

import dev.zephyr.Zephyr
import dev.zephyr.extensions.bukkit.PluginManager
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import java.util.function.Consumer

//@Suppress("UNCHECKED_CAST")
@KotlinOpens
class BukkitEventContext : EventContext, Listener {

    override val filters = mutableListOf<EventFilter>()
    override val forks = mutableSetOf<EventContext>()

    override fun filter(filter: EventFilter) = apply { filters.add(filter) }

    override fun onListeners(vararg listeners: Any) = listeners.forEach { listener ->
        //todo: make with mh proxy
        PluginManager.registerEvents(listener as Listener, Zephyr.Plugin)
    }

    override fun <E : Event> on(
        type: Class<E>,
        priority: EventPriority,
        ignoreCancelled: Boolean,
        handler: Consumer<E>
    ) = PluginManager.registerEvent(
        type, this, priority, { _, event ->
            if (type == event.javaClass) runCatching {
                handler.accept(event as E)
            }.exceptionOrNull()?.printStackTrace()
        }, Zephyr.Plugin, ignoreCancelled
    )

    override fun close() = HandlerList.unregisterAll(this).run {
        forks.onEach(EventContext::close).clear()
    }

    override fun fork(follow: Boolean) = BukkitEventContext().also {
        if (follow) {
            forks.add(it)
            it.filters.addAll(filters)
        }
    }

}