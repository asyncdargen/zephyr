package dev.zephyr.task

interface Task {

    val id: Int
    val context: TaskContext

    val delay: Int
    val period: Int
    var periods: Int
    val sync: Boolean

    val action: (Task) -> Unit
    var onTerminate: (Task) -> Unit

    val isRunning: Boolean

    fun terminate()

    fun cancel()

}

infix fun Task.onTerminate(block: (Task) -> Unit) = apply { onTerminate = block }
