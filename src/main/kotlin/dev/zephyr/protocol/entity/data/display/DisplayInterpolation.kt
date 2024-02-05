package dev.zephyr.protocol.entity.data.display

import dev.zephyr.protocol.entity.modify
import dev.zephyr.protocol.entity.world.display.ProtocolDisplay
import dev.zephyr.task.Task
import dev.zephyr.task.terminate
import dev.zephyr.util.bukkit.afterAsync
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.collection.SwitchableList
import dev.zephyr.util.kotlin.KotlinOpens
import java.time.Duration.between
import java.time.Instant
import java.time.Instant.now
import kotlin.reflect.KMutableProperty0


interface DisplayInterpolation<D : ProtocolDisplay> {

    val entity: ProtocolDisplay

    val duration: Long
    val nextInterpolation: DisplayInterpolation<D>
    val isRunning: Boolean

    fun runIfNoPreviousInterpolation(): DisplayInterpolation<D> = apply {
        if (!entity.interpolation.isRunning) {
            run()
        }
    }

    fun run(): DisplayInterpolation<D>

    fun cancel()

    infix fun after(block: D.() -> Unit): DisplayInterpolation<D>

    //return new interpolation
    infix fun next(block: D.() -> DisplayInterpolation<D>): DisplayInterpolation<D>

    object NOOP : DisplayInterpolation<ProtocolDisplay> {

        override val entity
            get() = throw UnsupportedOperationException()
        override val nextInterpolation: DisplayInterpolation<ProtocolDisplay>
            get() = throw UnsupportedOperationException()
        override val duration
            get() = throw UnsupportedOperationException()
        override val isRunning
            get() = false

        override fun cancel() {

        }

        override fun run(): DisplayInterpolation<ProtocolDisplay> {
            throw UnsupportedOperationException()
        }

        override fun after(block: (ProtocolDisplay) -> Unit): DisplayInterpolation<ProtocolDisplay> {
            throw UnsupportedOperationException()
        }

        override fun next(block: ProtocolDisplay.() -> DisplayInterpolation<ProtocolDisplay>): DisplayInterpolation<ProtocolDisplay> {
            throw UnsupportedOperationException()
        }


    }

}

@KotlinOpens
data class DisplayDelayedInterpolation<D : ProtocolDisplay>(
    override val entity: D,
    val delay: Long, override val duration: Long,
    val action: () -> Unit
) : DisplayInterpolation<D> {

    protected lateinit var afterAction: D.() -> Unit
    override lateinit var nextInterpolation: DisplayInterpolation<D>
    protected lateinit var timestamp: Instant

    protected var running = false
    protected var cancelled = false
    protected lateinit var task: Task

    override val isRunning
        get() = running && (this::task.isInitialized && !task.isCancelled
                || this::timestamp.isInitialized && between(timestamp, now()).toMillis() / 50 <= duration)

    override fun after(block: D.() -> Unit) = apply { afterAction = block }

    override fun next(block: D.() -> DisplayInterpolation<D>) = block(entity).apply(this::nextInterpolation::set)

    override fun run(): DisplayInterpolation<D> = apply {
        running = true
        entity.interpolation = this
        task = createTask()
    }

    fun process() {
        if (cancelled) return
        timestamp = now()

        entity.modify {
            interpolationDuration = duration.toInt()

            action()
        }
    }

    protected fun processAfterBlock() {
        if (!cancelled) {
            if (this@DisplayDelayedInterpolation::afterAction.isInitialized) {
                afterAction(entity)
            }
            if (this::nextInterpolation.isInitialized) {
                nextInterpolation.run()
            }
        }
    }

    override fun cancel() {
        cancelled = true
        task.cancel()
    }

    fun createTask() =
        dev.zephyr.util.bukkit.after(delay) { process() } terminate { afterAsync(duration) { processAfterBlock() } }

}

@KotlinOpens
class DisplayCycleInterpolation<D : ProtocolDisplay>(
    entity: D,
    delay: Long, duration: Long, val cycles: Int,
    action: () -> Unit
) : DisplayDelayedInterpolation<D>(entity, delay, duration, action) {

    override fun createTask() = everyAsync(delay, duration, cycles) { process() } terminate { processAfterBlock() }

}

@KotlinOpens
class DisplaySwitchableInterpolation : () -> Unit {

    private val actions = mutableSetOf<() -> Unit>()

    fun <V> KMutableProperty0<V>.switch(states: SwitchableList<V>) = apply {
        actions.add { set(states.switch()) }
    }

    fun <V> KMutableProperty0<V>.switch(vararg states: V) = switch(SwitchableList(states))

    fun <V> KMutableProperty0<V>.switch(second: V) = switch(get(), second)

    override fun invoke() {
        actions.forEach { it() }
    }

}