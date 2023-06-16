package dev.zephyr.extensions.java

inline fun Throwable?.throwIfNonNull() = this?.let { throw it }