package dev.zephyr.util.time

import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val DelayMap = WeakHashMap<String, Long>()

fun getDelay(id: String) = DelayMap[id]?.let { it - System.currentTimeMillis() } ?: 0

fun setDelay(id: String, duration: Duration) {
    DelayMap[id.lowercase()] = currentMillis + duration.inWholeMilliseconds
}

fun setDelay(id: String, duration: Long) =
    setDelay(id, duration.milliseconds)

fun hasDelay(id: String) = (DelayMap[id.lowercase()] ?: 0) > currentMillis

fun removeDelay(id: String) = DelayMap.remove(id.lowercase())

fun ifNotHasDelay(id: String, block: () -> Unit) {
    if (!hasDelay(id)) block()
}

fun checkOrSetDelay(id: String, duration: Duration): Boolean {
    if (hasDelay(id)) {
        return true
    }

    setDelay(id, duration)

    return false
}

fun checkOrSetDelay(id: String, duration: Long) =
    checkOrSetDelay(id, duration.milliseconds)

fun checkOrSetDelay(id: String, duration: Duration, block: () -> Unit): Boolean =
    checkOrSetDelay(id, duration).apply { if (!this) block() }

fun checkOrSetDelay(id: String, duration: Long, block: () -> Unit) =
    checkOrSetDelay(id, duration.milliseconds, block)

fun checkAndSetDelay(id: String, duration: Duration) = hasDelay(id).apply { setDelay(id, duration) }

fun checkAndSetDelay(id: String, duration: Long) =
    checkAndSetDelay(id, duration.milliseconds)

