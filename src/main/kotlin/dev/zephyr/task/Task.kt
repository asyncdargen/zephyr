package dev.zephyr.task

interface Task {

    val id: Int
    val context: TaskContext

    val delay: Long
    val period: Long
    val repeats: Int
    val executions: Int

    var action: (Task) -> Unit
    var terminationHandler: (Task) -> Unit

    val isSync: Boolean
    val isCancelled: Boolean

    fun execute()

    fun cancel()

}

infix fun Task.terminate(block: (Task) -> Unit) = apply { terminationHandler = block }
