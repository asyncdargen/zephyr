package dev.zephyr.event

import dev.zephyr.Zephyr
import dev.zephyr.util.bukkit.PluginManager
import dev.zephyr.util.java.catch
import dev.zephyr.util.java.unreflect
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.cast
import org.bukkit.event.*
import org.bukkit.plugin.EventExecutor
import java.lang.invoke.MethodHandleProxies
import java.util.function.Consumer

@Suppress("UNCHECKED_CAST")
@KotlinOpens
class BukkitEventContext : EventContext, Listener {

    override val filters = mutableListOf<EventFilter>()
    override val forks = mutableSetOf<EventContext>()

    override fun filter(filter: EventFilter) = apply { filters.add(filter) }

    override fun onListeners(vararg listeners: Any) = listeners.forEach { listener ->
        listener.javaClass.declaredMethods
            .filter {
                it.isAnnotationPresent(EventHandler::class.java)
                        && it.parameterCount == 1
                        && Event::class.java.isAssignableFrom(it.parameterTypes[0])
            }.forEach {
                val (p, ic) = it.getDeclaredAnnotation(EventHandler::class.java).run { priority to ignoreCancelled }
                val eventType = it.parameterTypes[0] as Class<Event>
                val handler = MethodHandleProxies.asInterfaceInstance(
                    Consumer::class.java,
                    it.unreflect().bindTo(listener)
                ) as Consumer<Event>

                on(eventType, p, ic, false) { handler.accept(this) }
            }
        PluginManager.registerEvents(listener as Listener, Zephyr.Plugin)
    }

    override fun <E : Event> on(
        type: Class<E>, priority: EventPriority,
        ignoreCancelled: Boolean, dependEvent: Boolean,
        handler: E.() -> Unit
    ) = PluginManager.registerEvent(
        type, this, priority,
        eventHandler(type, dependEvent, handler.cast()),
        Zephyr.Plugin, ignoreCancelled
    )

    override fun close() {
        HandlerList.unregisterAll(this)
        forks.onEach(EventContext::close).clear()
    }

    override fun fork(follow: Boolean) = BukkitEventContext().also {
        if (follow) {
            forks.add(it)
            it.filters.addAll(filters)
        }
    }

    private fun eventHandler(
        type: Class<out Event>,
        dependEvent: Boolean, handler: (Event) -> Unit
    ) = EventExecutor { _, event ->
        val eventType = event.javaClass
        if ((dependEvent && type.isAssignableFrom(eventType) || type == eventType) && filters.all { it(event) }) {
            catch("Error while event handle") { handler(event) }
        }
    }

}