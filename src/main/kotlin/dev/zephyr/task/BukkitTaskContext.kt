package dev.zephyr.task

import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf

class BukkitTaskContext : TaskContext {

    override val forks = concurrentSetOf<TaskContext>()
    override val tasks = concurrentHashMapOf<Int, Task>()

    override fun run(sync: Boolean, delay: Int, period: Int, periods: Int, handler: (Task) -> Unit) =
        BukkitTask(delay, period, periods, sync, this, handler).apply { tasks[id] =  this }

    override fun close() = tasks.values.forEach(Task::cancel).run { forks.onEach(TaskContext::close).clear() }

    override fun fork(follow: Boolean) = BukkitTaskContext().also { if (follow) forks.add(it) }

}