package dev.zephyr.util.collection

import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class ObservableMap<K, V>(
    val map: MutableMap<K, V>,
    val handleAdd: (K, V) -> Unit,
    val handleRemove: (K, V?) -> Unit
) : MutableMap<K, V> by map {

    override fun put(key: K, value: V) = map.put(key, value).apply { handleAdd(key, value) }

    override fun putAll(from: Map<out K, V>) = map.putAll(from).apply { forEach(handleAdd) }

    override fun remove(key: K) = map.remove(key).apply { handleRemove(key, this) }

    override fun remove(key: K, value: V) = map.remove(key, value).apply { handleRemove(key, value) }

}