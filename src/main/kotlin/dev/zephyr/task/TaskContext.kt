package dev.zephyr.task

import dev.zephyr.util.function.ForkingContext

interface TaskContext : ForkingContext<TaskContext> {

    val tasks: MutableMap<Int, Task>

    fun after(handler: (Task) -> Unit) = run(true, 1, 1, 1, handler)

    fun afterAsync(handler: (Task) -> Unit) = run(false, 1, 1, 1, handler)

    fun after(delay: Int, handler: (Task) -> Unit) = run(true, delay, delay, 1, handler)

    fun afterAsync(delay: Int, handler: (Task) -> Unit) = run(false, delay, delay, 1, handler)

    fun every(delay: Int, period: Int, handler: (Task) -> Unit) = run(true, delay, period, -1, handler)

    fun everyAsync(delay: Int, period: Int, handler: (Task) -> Unit) = run(false, delay, period, -1, handler)

    fun every(delay: Int, period: Int, periods: Int, handler: (Task) -> Unit) = run(true, delay, period, periods, handler)

    fun everyAsync(delay: Int, period: Int, periods: Int, handler: (Task) -> Unit) = run(false, delay, period, periods, handler)

    fun run(sync: Boolean, delay: Int, period: Int, periods: Int, handler: (Task) -> Unit): Task

}