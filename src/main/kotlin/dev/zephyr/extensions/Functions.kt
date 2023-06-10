package dev.zephyr.extensions

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader

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