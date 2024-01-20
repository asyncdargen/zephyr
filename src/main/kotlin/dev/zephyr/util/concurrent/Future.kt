package dev.zephyr.util.concurrent

import dev.zephyr.util.bukkit.forceMainThread
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

val Executor = Executors.newScheduledThreadPool(2)

fun async(block: () -> Unit) = Executor.execute(block)

fun <T> future(block: CompletableFuture<T>.() -> Unit) = CompletableFuture<T>().apply(block)

infix fun <T> CompletableFuture<T>.sync(block: (T) -> Unit) = thenAccept { forceMainThread { block(it) } }