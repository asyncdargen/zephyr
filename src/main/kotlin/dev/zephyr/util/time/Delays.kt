package dev.zephyr.util.time

import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

val DelayMap = WeakHashMap<String, Long>()

fun setDelay(id: String, duration: Duration) {
    DelayMap[id.lowercase()] = currentMillis() + duration.inWholeMilliseconds
}

fun setDelay(id: String, duration: Long) =
    setDelay(id, duration.milliseconds)

fun hasDelay(id: String) = (DelayMap[id.lowercase()] ?: 0) > currentMillis()

fun removeDelay(id: String) = DelayMap.remove(id.lowercase())

fun checkOrSetDelay(id: String, duration: Duration): Boolean {
    if (hasDelay(id)) {
        return true
    }

    setDelay(id, duration)

    return false
}

fun checkOrSetDelay(id: String, duration: Long) =
    checkOrSetDelay(id, duration.milliseconds)

fun checkAndSetDelay(id: String, duration: Duration) = hasDelay(id).apply { setDelay(id, duration) }

fun checkAndSetDelay(id: String, duration: Long) =
    checkAndSetDelay(id, duration.milliseconds)

