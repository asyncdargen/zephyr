package dev.zephyr.util.kotlin

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader

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