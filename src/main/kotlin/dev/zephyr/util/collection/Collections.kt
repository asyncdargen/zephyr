package dev.zephyr.util.collection

import java.util.*
import java.util.concurrent.ConcurrentHashMap

fun <T, V> MutableCollection<V>.mirror(extractor: (V) -> T, mapper: (T) -> V): MutableCollection<T> =
    CollectionMirror(this, extractor, mapper)

fun <T, C : Collection<T>> C.sync() = Collections.synchronizedCollection(this)

fun <K, V, M : Map<K, V>> M.sync() = Collections.synchronizedMap(this)

fun <T> concurrentSetOf(vararg objects: T): MutableSet<T> =
    concurrentSetOf<T>().apply { addAll(objects) }

fun <T> concurrentSetOf(): MutableSet<T> =
    Collections.newSetFromMap(ConcurrentHashMap())

fun <K, V> concurrentHashMapOf(vararg entries: Pair<K, V>): MutableMap<K, V> =
    concurrentHashMapOf<K, V>().apply { putAll(entries) }

fun <K, V> concurrentHashMapOf(): MutableMap<K, V> =
    ConcurrentHashMap<K, V>()

inline fun <K, reified R> Map<K, *>.filterValuesIsInstance() =
    filterValues { it is R }.mapValues { (_, value) -> value as R }

fun <C : Collection<*>> C.takeIfNotEmpty() = takeIf(Collection<*>::isNotEmpty)

inline fun <C : Collection<*>, T> C.ifNotEmpty(block: (C) -> T) = if (isNotEmpty()) block(this) else null

fun <V> Iterable<V>.repeat(times: Int) = buildList { repeat(times) no@{ addAll(this@repeat) } }

operator fun <V> Iterable<V>.times(times: Int) = repeat(times)

fun <E> MutableCollection<E>.observe(addHandler: (E) -> Unit, removeHandler: (E) -> Unit): MutableCollection<E> =
    ObservableCollection(this, addHandler, removeHandler)

fun <K, V> MutableMap<K, V>.observe(addHandler: (K, V) -> Unit, removeHandler: (K, V?) -> Unit): MutableMap<K, V> =
    ObservableMap(this, addHandler, removeHandler)

inline fun <F, S> forEach2d(first: Iterable<F>, second: Iterable<S>, block: (F, S) -> Unit) =
    first.forEach { first -> second.forEach { second -> block(first, second) } }

inline fun <F, S, T> forEach3d(first: Iterable<F>, second: Iterable<S>, third: Iterable<T>, block: (F, S, T) -> Unit) =
    first.forEach { first -> second.forEach { second -> third.forEach { third -> block(first, second, third) } } }
