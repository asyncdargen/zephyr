package dev.zephyr.protocol.scoreboard.builder

import dev.zephyr.protocol.scoreboard.ProtocolScoreboard
import dev.zephyr.util.component.asComponent
import dev.zephyr.util.component.components
import dev.zephyr.util.component.literal
import dev.zephyr.util.component.toComponent
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.map
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolScoreboardBuilder<U>(var updaterEntity: (Player) -> U) {

    var title: Component = Component.text("")
    var titleString: String
        set(value) {
            title = value.toComponent()
        }
        get() = title.literal()

    val lines = mutableMapOf<Int, ProtocolScoreboardLine>()

    var invertIndexes = true
    val updaters = mutableSetOf<Pair<Long, ProtocolScoreboard.(Player) -> Unit>>()

    fun title(component: Component) = apply { this.title = title }

    fun title(title: String) = title(title.toComponent())

    fun line(index: Int, line: ProtocolScoreboardLine) = apply { lines[index] = line }

    fun line(line: ProtocolScoreboardLine) = line((lines.keys.maxOrNull() ?: 0) + 1, line)

    fun line(index: Int, line: Component) = line(index, StaticProtocolScoreboardLine(line))

    fun line(index: Int, line: String) = line(index, line.toComponent())

    fun line(line: Component) = line(StaticProtocolScoreboardLine(line))

    fun line(line: String) = line(line.toComponent())

    fun line(index: Int, interval: Long = 20, lineSupplier: (U) -> Component) =
        line(index, LazyProtocolScoreboardLine(interval) { lineSupplier(updaterEntity(it)) })

    fun lineString(index: Int, interval: Long = 20, lineSupplier: (U) -> String) =
        line(index, interval, lineSupplier.map(String::toComponent))

    fun line(interval: Long = 20, supplier: (U) -> Component) =
        line(LazyProtocolScoreboardLine(interval) { supplier(updaterEntity(it)) })

    fun lineString(interval: Long = 20, lineSupplier: (U) -> String) =
        line(interval, lineSupplier.map(String::toComponent))

    fun line(
        index: Int,
        prefix: Component, suffix: Component = Component.empty(),
        interval: Long = 20, lineSupplier: (U) -> Component
    ) = line(index, LazyWrappedProtocolScoreboardLine(prefix, suffix, interval) { lineSupplier(updaterEntity(it)) })

    fun lineString(
        index: Int,
        prefix: String, suffix: String = "",
        interval: Long = 20, lineSupplier: (U) -> String
    ) = line(index, prefix.toComponent(), suffix.toComponent(), interval, lineSupplier.map(String::toComponent))

    fun line(
        prefix: Component, suffix: Component = Component.empty(),
        interval: Long = 20, lineSupplier: (U) -> Component
    ) = line(LazyWrappedProtocolScoreboardLine(prefix, suffix, interval) { lineSupplier(updaterEntity(it)) })

    fun lineString(
        prefix: String, suffix: String = "",
        interval: Long = 20, lineSupplier: (U) -> String
    ) = line(prefix.toComponent(), suffix.toComponent(), interval, lineSupplier.map(String::toComponent))

    fun line(index: Int) = line(index, EmptyProtocolScoreboardLine)

    fun line() = line(EmptyProtocolScoreboardLine)

    fun lines(vararg lines: Any) = lines.map {
        when (it) {
            is ProtocolScoreboardLine -> it
            is Component -> StaticProtocolScoreboardLine(it)
            is String -> if (it.isEmpty()) EmptyProtocolScoreboardLine else StaticProtocolScoreboardLine(it.toComponent())
            else -> StaticProtocolScoreboardLine(it.asComponent())
        }
    }.forEach(this::line)

    fun updater(interval: Long = 20, block: ProtocolScoreboard.(Player) -> Unit) =
        apply { updaters.add(interval to block) }

    operator fun String.invoke(interval: Long = 20, block: (U) -> Any) =
        LazyWrappedProtocolScoreboardLine(toComponent(), Component.empty(), interval) { block(updaterEntity(it)).asComponent() }

    operator fun Component.invoke(interval: Long = 20, block: (U) -> Any) =
        LazyWrappedProtocolScoreboardLine(this, Component.empty(), interval) { block(updaterEntity(it)).asComponent() }


    interface ProtocolScoreboardLine {

        fun getContent(player: Player): Component

    }

    @KotlinOpens
    class StaticProtocolScoreboardLine(private val content: Component) : ProtocolScoreboardLine {

        override fun getContent(player: Player) = content

    }

    object EmptyProtocolScoreboardLine : StaticProtocolScoreboardLine(Component.empty())

    @KotlinOpens
    class LazyProtocolScoreboardLine(val interval: Long, val supplier: (Player) -> Component) : ProtocolScoreboardLine {

        override fun getContent(player: Player) = supplier(player)

    }

    @KotlinOpens
    class LazyWrappedProtocolScoreboardLine(
        val prefix: Component,
        val suffix: Component,
        interval: Long,
        supplier: (Player) -> Component
    ) : LazyProtocolScoreboardLine(interval, supplier) {

        val base = components(prefix, suffix)

        override fun getContent(player: Player) =
            base.replaceText { it.matchLiteral("{}").replacement(super.getContent(player)) }

    }


}

fun <P> scoreboard(updaterEntity: (Player) -> P, block: ProtocolScoreboardBuilder<P>.() -> Unit) =
    ProtocolScoreboardBuilderHolder(block, updaterEntity)

fun scoreboard(block: ProtocolScoreboardBuilder<Player>.() -> Unit) =
    scoreboard({ it }, block)

