package dev.zephyr.protocol.scoreboard.builder

import dev.zephyr.protocol.scoreboard.ProtocolScoreboard
import dev.zephyr.protocol.scoreboard.ScoreboardProtocol
import dev.zephyr.util.collection.filterValuesIsInstance
import dev.zephyr.util.component.toComponent
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolScoreboardBuilder() {
    constructor(block: ProtocolScoreboardBuilder.() -> Unit) : this() {
        block(this)
    }

    var title = "Scoreboard"
    val lines = mutableMapOf<Int, ProtocolScoreboardLine>()

    var invertIndexes = true
    val updaters = mutableSetOf<Pair<Int, ProtocolScoreboard.(Player) -> Unit>>()

    fun title(title: String) = apply { this.title = title }

    fun line(index: Int, line: ProtocolScoreboardLine) = apply { lines[index] = line }

    fun line(line: ProtocolScoreboardLine) = line((lines.keys.maxOrNull() ?: 0) + 1, line)

    fun line(index: Int, line: String) = line(index, StaticProtocolScoreboardLine(line))

    fun line(line: String) = line(StaticProtocolScoreboardLine(line))

    fun line(index: Int, interval: Int = 20, lineSupplier: (Player) -> String) =
        line(index, LazyProtocolScoreboardLine(interval, lineSupplier))

    fun line(interval: Int = 20, lineSupplier: (Player) -> String) =
        line(LazyProtocolScoreboardLine(interval, lineSupplier))

    fun line(index: Int, prefix: String, suffix: String = "", interval: Int = 20, lineSupplier: (Player) -> String) =
        line(index, LazyWrappedProtocolScoreboardLine(prefix, suffix, interval, lineSupplier))

    fun line(prefix: String, suffix: String = "", interval: Int = 20, lineSupplier: (Player) -> String) =
        line(LazyWrappedProtocolScoreboardLine(prefix, suffix, interval, lineSupplier))

    fun line(index: Int) = line(index, EmptyProtocolScoreboardLine)

    fun line() = line(EmptyProtocolScoreboardLine)

    fun updater(interval: Int = 20, block: ProtocolScoreboard.(Player) -> Unit) =
        apply { updaters.add(interval to block) }

    fun create(player: Player): ProtocolScoreboard {
        return ProtocolScoreboard(title).apply {
            val lines = this@ProtocolScoreboardBuilder.lines.run {
                if (invertIndexes) {
                    val maxIndex = keys.maxOrNull() ?: 0
                    mapKeys { (index, _) -> maxIndex - index }
                } else this
            }

            title = this@ProtocolScoreboardBuilder.title.toComponent()

            lines.filterValuesIsInstance<Int, StaticProtocolScoreboardLine>().forEach { (index, line) ->
                setLine(index, line.getContent(player))
            }

            lines.filterValuesIsInstance<Int, LazyProtocolScoreboardLine>().forEach { (index, line) ->
                taskContext.everyAsync(0, line.interval) { setLine(index, line.getContent(player)) }
            }

            updaters.forEach { (interval, updater) ->
                taskContext.everyAsync(0, interval) { updater(this, player) }
            }

            ScoreboardProtocol[player] = this
            spawn(player)
        }
    }

    fun bind() = ScoreboardProtocol.bind(this)

    interface ProtocolScoreboardLine {

        fun getContent(player: Player): String

    }

    @KotlinOpens
    class StaticProtocolScoreboardLine(private val content: String) : ProtocolScoreboardLine {

        override fun getContent(player: Player) = content

    }

    object EmptyProtocolScoreboardLine : StaticProtocolScoreboardLine("")

    @KotlinOpens
    class LazyProtocolScoreboardLine(val interval: Int, val supplier: (Player) -> String) : ProtocolScoreboardLine {

        override fun getContent(player: Player) = supplier(player)

    }

    @KotlinOpens
    class LazyWrappedProtocolScoreboardLine(val prefix: String, val suffix: String, interval: Int, supplier: (Player) -> String) :
        LazyProtocolScoreboardLine(interval, supplier) {

        override fun getContent(player: Player) = "$prefix${super.getContent(player)}$suffix"

    }


}

fun scoreboard(block: ProtocolScoreboardBuilder.() -> Unit) =
    ProtocolScoreboardBuilder(block)