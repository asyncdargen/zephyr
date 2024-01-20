@file:Suppress("DEPRECATION")

package dev.zephyr.task

import dev.zephyr.util.concurrent.future
import dev.zephyr.util.java.Lookup
import dev.zephyr.util.kotlin.safeCast
import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit
import java.util.*

object MinecraftTaskProcessor {

    val Tasks: Queue<Runnable> = Lookup.findGetter(MinecraftServer::class.java, "processQueue", Queue::class.java)
        .bindTo(MinecraftServer.getServer())
        .invoke().safeCast<Queue<Runnable>>()
        ?: throw IllegalStateException("No such minecraft tasks queue field")
    val IsMainThread get() = Bukkit.isPrimaryThread()

    fun post(task: () -> Unit) {
        Tasks.add(task)
    }

    fun <T> postCallback(task: () -> T) =
        future { post { runCatching(task::invoke).fold(this::complete, this::completeExceptionally) } }

    fun forceMainThread(task: () -> Unit) = if (IsMainThread) task() else post(task)

    fun <T> forceMainThreadCallback(task: () -> T) =
        future { forceMainThread { runCatching(task::invoke).fold(this::complete, this::completeExceptionally) } }

}