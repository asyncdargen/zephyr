package dev.zephyr.util.bukkit

import dev.zephyr.task.GlobalTaskContext
import dev.zephyr.task.MinecraftTaskProcessor
import dev.zephyr.task.Task

fun after(handler: (Task) -> Unit) = GlobalTaskContext.after(handler)

fun afterAsync(handler: (Task) -> Unit) = GlobalTaskContext.afterAsync(handler)

fun after(delay: Long, handler: (Task) -> Unit) = GlobalTaskContext.after(delay, handler)

fun afterAsync(delay: Long, handler: (Task) -> Unit) = GlobalTaskContext.afterAsync(delay, handler)

fun every(delay: Long, period: Long, handler: (Task) -> Unit) = GlobalTaskContext.every(delay, period, handler)

fun everyAsync(delay: Long, period: Long, handler: (Task) -> Unit) = GlobalTaskContext.everyAsync(delay, period, handler)

fun every(delay: Long, period: Long, repeats: Int, handler: (Task) -> Unit) = GlobalTaskContext.every(delay, period, repeats, handler)

fun everyAsync(delay: Long, period: Long, repeats: Int, handler: (Task) -> Unit) = GlobalTaskContext.everyAsync(delay, period, repeats, handler)

fun sync(task: () -> Unit) = forceMainThread(task)

fun postMainThread(task: () -> Unit) = MinecraftTaskProcessor.post(task)

fun forceMainThread(task: () -> Unit) = MinecraftTaskProcessor.forceMainThread(task)

fun <T> postMainThreadCallback(task: () -> T) = MinecraftTaskProcessor.postCallback(task)

fun <T> forceMainThreadCallback(task: () -> T) = MinecraftTaskProcessor.forceMainThreadCallback(task)