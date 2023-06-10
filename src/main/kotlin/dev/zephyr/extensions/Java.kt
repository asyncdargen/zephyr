package dev.zephyr.extensions

inline fun Throwable?.throwIfNonNull() = this?.let { throw it }