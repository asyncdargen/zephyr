package dev.zephyr.task

import dev.zephyr.util.function.ForkingContext

interface TaskContext : ForkingContext<TaskContext> {

    val tasks: MutableMap<Int, out Task>

    fun after(handler: (Task) -> Unit) = run(true, 1, 1, 1, handler)

    fun afterAsync(handler: (Task) -> Unit) = run(false, 1, 1, 1, handler)

    fun after(delay: Long, handler: (Task) -> Unit) = run(true, delay, delay, 1, handler)

    fun afterAsync(delay: Long, handler: (Task) -> Unit) = run(false, delay, delay, 1, handler)

    fun every(delay: Long, period: Long, handler: (Task) -> Unit) = run(true, delay, period, -1, handler)

    fun everyAsync(delay: Long, period: Long, handler: (Task) -> Unit) = run(false, delay, period, -1, handler)

    fun every(delay: Long, period: Long, repeats: Int, handler: (Task) -> Unit) = run(true, delay, period, repeats, handler)

    fun everyAsync(delay: Long, period: Long, repeats: Int, handler: (Task) -> Unit) = run(false, delay, period, repeats, handler)

    fun run(isSync: Boolean, delay: Long, period: Long, repeats: Int, handler: (Task) -> Unit): Task

}