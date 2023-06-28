package dev.zephyr.util.numbers

fun Int.hex() = Integer.toHexString(this)

inline fun <N> N.isBetween(min: N, max: N) where N : Number, N : Comparable<N> = this >= min && this <= max