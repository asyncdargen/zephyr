package dev.zephyr.task

import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class BukkitTaskContext : TaskContext {

    override val forks = concurrentSetOf<TaskContext>()
    override val tasks = concurrentHashMapOf<Int, Task>()

    override fun run(isSync: Boolean, delay: Long, period: Long, repeats: Int, handler: (Task) -> Unit) =
        BukkitTask(delay, period, repeats, isSync, this, handler).apply { tasks[id] = this }

    override fun close() {
        tasks.values.forEach(Task::cancel)
        forks.onEach(TaskContext::close).clear()
    }

    override fun fork(follow: Boolean) = BukkitTaskContext().also { if (follow) forks.add(it) }

}