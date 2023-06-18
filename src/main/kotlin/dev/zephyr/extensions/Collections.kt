package dev.zephyr.extensions

import java.util.*
import java.util.concurrent.ConcurrentHashMap

fun <T> concurrentSetOf(vararg objects: T): MutableSet<T> =
    Collections.newSetFromMap<T>(ConcurrentHashMap()).apply { addAll(objects.toList()) }

fun <K, V> concurrentHashMapOf(vararg entries: Pair<K, V>): MutableMap<K, V> =
    ConcurrentHashMap<K, V>().apply { putAll(entries) }

inline fun <K, reified R> Map<K, *>.filterValuesInInstance() =
    filterValues { it is R }.mapValues { (_, value) -> value as R }

fun <C : Collection<*>> C.takeIfNotEmpty() = takeIf(Collection<*>::isNotEmpty)

inline fun <C : Collection<*>, T> C.ifNotEmpty(block: (C) -> T) = block(this)