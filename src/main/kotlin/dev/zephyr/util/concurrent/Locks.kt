package dev.zephyr.util.concurrent

import java.util.concurrent.locks.Lock

inline fun Lock.use(block: () -> Unit) {
    try {
        lock()
        block()
    } finally {
        unlock()
    }
}