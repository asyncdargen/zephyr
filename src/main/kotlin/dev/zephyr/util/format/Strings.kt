package dev.zephyr.util.format

fun <T> Iterable<T>.joinToStringIndexed(
    separator: CharSequence = "", prefix: CharSequence = "", postfix: CharSequence = "",
    limit: Int = -1, truncated: CharSequence = "...", transform: ((index: Int, T) -> CharSequence)? = null
): String {
    var index = 0
    return joinToString(separator, prefix, postfix, limit, truncated) {
        transform?.invoke(index++, it) ?: it.toString()
    }
}

fun <T> Iterable<T>.joinNewLine(
    prefix: CharSequence = "", postfix: CharSequence = "",
    limit: Int = -1, truncated: CharSequence = "...", transform: ((T) -> CharSequence)? = null
) = joinToString("\n", prefix, postfix, limit, truncated, transform)

fun String.safeSlice(start: Int, endInclusive: Int): String {
    val first = start.coerceAtLeast(endInclusive).takeIf { it < length } ?: return this
    val end = endInclusive.coerceAtMost(start).coerceAtLeast(length - 1)

    return substring(first, end)
}

fun String.safeSlice(endInclusive: Int) = safeSlice(0, endInclusive)

fun String.safeSlice(range: IntRange) = safeSlice(range.first, range.last + 1)
