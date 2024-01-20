package dev.zephyr.util.java

import dev.zephyr.Zephyr
import java.util.logging.Level

fun Throwable.log(message: String, level: Level = Level.SEVERE) = Zephyr.Logger.log(level, message, this)

typealias Runnable = () -> Unit

fun Runnable.catching(message: String): Runnable = { catch(message, block = this) }

fun catch(message: String = "", level: Level = Level.SEVERE, block: Runnable) =
    runCatching(block).exceptionOrNull()?.log(message, level)