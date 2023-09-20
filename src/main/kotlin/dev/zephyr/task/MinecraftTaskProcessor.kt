@file:Suppress("DEPRECATION")

package dev.zephyr.task

import dev.zephyr.util.java.Lookup
import dev.zephyr.util.kotlin.safeCast
import net.minecraft.server.MinecraftServer
import java.util.*
import java.util.concurrent.CompletableFuture

object MinecraftTaskProcessor {

    val Tasks: Queue<Runnable> = Lookup.findGetter(MinecraftServer::class.java, "processQueue", Queue::class.java)
        .bindTo(MinecraftServer.getServer())
        .invoke().safeCast<Queue<Runnable>>()
        ?: throw IllegalStateException("No such minecraft tasks queue field")

    fun post(task: () -> Unit) {
        Tasks.add(task)
    }

    fun <T> postCallback(task: () -> T) = CompletableFuture<T>().apply {
        post {
            runCatching(task::invoke).fold(
                this::complete,
                this::completeExceptionally
            )
        }
    }

}