package dev.zephyr.task

import dev.zephyr.Zephyr.Plugin
import dev.zephyr.util.concurrent.threadLocal
import dev.zephyr.util.java.catch
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Bukkit.getScheduler
import java.util.logging.Level

val TickingTasks = threadLocal<Task>()

fun currentTask() = TickingTasks.get()

@KotlinOpens
class BukkitTask(
    override val delay: Long = 0L,
    override val period: Long = 0L,
    override val repeats: Int,
    override val isSync: Boolean,

    override val context: TaskContext,

    override var action: (Task) -> Unit,
    override var terminationHandler: (Task) -> Unit = { }
) : Task {

    private val handle = when {
        isSync && repeats == 1 -> getScheduler().runTaskLater(Plugin, this::execute, delay)
        isSync -> getScheduler().runTaskTimer(Plugin, this::execute, delay, period)

        repeats == 1 -> getScheduler().runTaskLaterAsynchronously(Plugin, this::execute, delay)
        else -> getScheduler().runTaskTimerAsynchronously(Plugin, this::execute, delay, period)
    }

    override val id get() = handle.taskId
    override val isCancelled get() = handle.isCancelled

    override var executions = 0

    override fun execute() {
        TickingTasks.set(this)
        ++executions
        catch("Error while task executing: ${handle?.taskId ?: "task handle is null"}", Level.WARNING) { action(this) }
        if (repeats in 0..executions) {
            cancel()
        }
        TickingTasks.remove()
    }

    override fun cancel() {
        if (/*repeats != 1 && */!isCancelled) { // useless check comment
            handle.cancel()
        }

        context.tasks.remove(id)
        catch("Error while processing task termination handler: ${handle?.taskId ?: "task handle is null"}") { terminationHandler(this) }
    }

}