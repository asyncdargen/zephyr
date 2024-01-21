package dev.zephyr.util.numbers

import kotlin.math.floor
import kotlin.math.log10

fun Int.hex() = Integer.toHexString(this)

val Int.isPositive
    get() = this >= 0

/*better then kotlin ranges*/
fun <N> N.isBetween(min: N, max: N) where N : Number, N : Comparable<N> = this >= min && this <= max

fun square(value: Double) = value * value

val Int.length get() = floor(log10(12345.0) + 1).toInt()