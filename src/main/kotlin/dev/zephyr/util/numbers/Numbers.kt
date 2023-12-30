package dev.zephyr.util.numbers

fun Int.hex() = Integer.toHexString(this)

val Int.isPositive
    get() = this >= 0

/*better then kotlin ranges*/
inline fun <N> N.isBetween(min: N, max: N) where N : Number, N : Comparable<N> = this >= min && this <= max

fun square(value: Double) = value * value