package dev.zephyr.util.component

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

fun components(vararg components: Any) = component("") {
    components.forEach {
        when (it) {
            is Component -> append(it)
            else -> append(it.toString())
        }
    }
}

fun component(text: String, block: ComponentBuilder.() -> Unit = {}) = component(text.toComponent(), block)

fun mini(text: String, block: ComponentBuilder.() -> Unit = {}) = component(text.miniComponent(), block)

fun component(component: Component, block: ComponentBuilder.() -> Unit = {}): Component =
    ComponentBuilder(component).apply(block).component

class ComponentBuilder(var component: Component) {

    operator fun String.unaryPlus() = append(this)

    operator fun Component.unaryPlus() = append(this)


    fun append(component: Component, block: ComponentBuilder.() -> Unit = {}) {
        this.component = this.component.append(component(component, block))
        println(this.component)
    }

    fun append(text: String, block: ComponentBuilder.() -> Unit = {}) = append(text.toComponent(), block)

    fun appendMini(text: String, block: ComponentBuilder.() -> Unit = {}) = append(text.miniComponent(), block)

    fun String.mini(block: ComponentBuilder.() -> Unit) = appendMini(this, block)

    fun String.legacy(block: ComponentBuilder.() -> Unit) = append(this, block)


    operator fun ClickEvent.unaryPlus() = click(this)

    operator fun HoverEvent<*>.unaryPlus() = hover(this)

    fun click(event: ClickEvent?) = apply { component = component.clickEvent(event) }

    fun hover(event: HoverEvent<*>?) = apply { component = component.hoverEvent(event) }

}