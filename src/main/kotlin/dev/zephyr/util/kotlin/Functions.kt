package dev.zephyr.util.kotlin

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader

typealias Function1<T, R> = (T) -> R
typealias DualPair<T> = Pair<T, T>

inline fun <T> T.repeatWhile(condition: (T) -> Boolean, block: (T) -> Unit) {
    while (condition(this)) {
        block(this)
    }
}

//Unit utils

inline fun unit(block: () -> Unit) {
    block.invoke()
}

fun <T> T.print(addition: String? = null): T {
    return apply { if (addition !== null) println("$addition: $this") else println(this) }
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

inline fun <reified C> Any.cast() = this as C

inline fun <reified C> Any?.safeCast() = this as? C

inline fun <reified C, T> T.castOr(default: (T) -> C) = this as? C ?: default(this)

inline fun Any?.takeIfEqual(any: Any?) = takeIf { it == any }

inline fun Any?.takeIfRef(any: Any?) = takeIf { it === any }

fun <T, R, M> Function1<T, R>.map(mapper: (R) -> M) = { it: T -> mapper(this(it)) }

fun <T, R, M> Function1<T, R>.mapFully(mapper: (T, R) -> M) = { it: T -> mapper(it, this(it)) }