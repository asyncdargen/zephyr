package dev.zephyr.util.bukkit

import dev.zephyr.task.GlobalTaskContext
import dev.zephyr.task.MinecraftTaskProcessor
import dev.zephyr.task.Task
import org.bukkit.Bukkit

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

fun forceInMainThread(task: () -> Unit) = if (Bukkit.isPrimaryThread()) task() else postToMainThread(task)

fun <T> postToMainThreadCallback(task: () -> T) = MinecraftTaskProcessor.postCallback(task)