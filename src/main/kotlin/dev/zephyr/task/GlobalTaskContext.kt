package dev.zephyr.task

var GlobalTaskContext: TaskContext = BukkitTaskContext()

fun TaskContext.makeGlobal() = ::GlobalTaskContext.set(this)