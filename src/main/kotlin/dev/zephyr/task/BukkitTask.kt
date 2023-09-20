package dev.zephyr.task

import dev.zephyr.Zephyr
import dev.zephyr.util.concurrent.threadLocal
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.logging.Level

val TickingTasks = threadLocal<Task>()

fun currentTask() = TickingTasks.get()

class BukkitTask(
    override val delay: Int,
    override val period: Int = 0,
    override var periods: Int,
    override val sync: Boolean,
    override val context: TaskContext,
    override val action: (Task) -> Unit,
    override var onTerminate: (Task) -> Unit = {  }
) : Task, BukkitRunnable() {

    private val handle: BukkitTask = when {
        sync -> runTaskTimer(Zephyr.Plugin, delay.toLong(), period.toLong())
        else -> runTaskTimerAsynchronously(Zephyr.Plugin, delay.toLong(), period.toLong())
    }

    override val id by handle::taskId
    override val isRunning get() = !isCancelled

    override fun terminate() {
        if (isRunning)
            handle.cancel()

        context.tasks.remove(id)

        onTerminate(this)
    }

    override fun run() {
        TickingTasks.set(this)
        runCatching(action::invoke).exceptionOrNull()?.let {
            Zephyr.Logger.log(Level.WARNING, "Error while executing task ${handle.taskId}", it)
        }
        TickingTasks.remove()

        if (periods == 0 || --periods == 0) terminate()
    }

    override fun cancel() {
        terminate()
    }

}