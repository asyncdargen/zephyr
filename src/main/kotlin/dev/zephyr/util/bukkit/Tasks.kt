package dev.zephyr.util.bukkit

import dev.zephyr.task.GlobalTaskContext
import dev.zephyr.task.Task
import dev.zephyr.task.MinecraftTaskProcessor

fun after(handler: (Task) -> Unit) = GlobalTaskContext.after(handler)

fun afterAsync(handler: (Task) -> Unit) = GlobalTaskContext.afterAsync(handler)

fun after(delay: Int, handler: (Task) -> Unit) = GlobalTaskContext.after(delay, handler)

fun afterAsync(delay: Int, handler: (Task) -> Unit) = GlobalTaskContext.afterAsync(delay, handler)

fun every(delay: Int, period: Int, handler: (Task) -> Unit) = GlobalTaskContext.every(delay, period, handler)

fun everyAsync(delay: Int, period: Int, handler: (Task) -> Unit) = GlobalTaskContext.everyAsync(delay, period, handler)

fun every(delay: Int, period: Int, periods: Int, handler: (Task) -> Unit) = GlobalTaskContext.every(delay, period, periods, handler)

fun everyAsync(delay: Int, period: Int, periods: Int, handler: (Task) -> Unit) = GlobalTaskContext.everyAsync(delay, period, periods, handler)

fun sync(task: () -> Unit) = postToMainThread(task)

fun postToMainThread(task: () -> Unit) = MinecraftTaskProcessor.post(task)

fun <T> postToMainThreadCallback(task: () -> T) = MinecraftTaskProcessor.postCallback(task)