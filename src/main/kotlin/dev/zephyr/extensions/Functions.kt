package dev.zephyr.extensions

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader

inline fun <T> T.repeatWhile(condition: (T) -> Boolean, block: (T) -> Unit) {
    while (condition(this)) {
        block(this)
    }
}

//Unit utils

inline fun unit(block: () -> Unit) {
    block.invoke()
}

inline fun Any?.unit(block: () -> Unit) {
    block.invoke()
}

fun Any?.unit() {}

fun <K, V> CacheBuilder<Any, Any>.build(loader: (K) -> V) = build(object : CacheLoader<K, V>() {
    override fun load(key: K) = loader(key)
})

fun <V : Comparable<V>> Pair<V, V>.max() = maxOf(first, second)

fun <V : Comparable<V>> Pair<V, V>.min() = minOf(first, second)

fun <T> Iterable<T>.joinToStringIndexed(
    separator: CharSequence = "", prefix: CharSequence = "", postfix: CharSequence = "",
    limit: Int = -1, truncated: CharSequence = "...", transform: ((index: Int, T) -> CharSequence)? = null
): String {
    var index = 0
    return joinToString(separator, prefix, postfix, limit, truncated) {
        transform?.invoke(index++, it) ?: it.toString()
    }
}