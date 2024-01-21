package dev.zephyr.util.item

import dev.zephyr.util.bukkit.namespaceKey
import dev.zephyr.util.item.tag.ItemStackTagTypeMatcher
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType


typealias TagType<T> = PersistentDataType<*, T>

fun <T> ItemStack.tag(key: NamespacedKey, type: TagType<T>) =
    itemMeta?.persistentDataContainer?.get(key, type)

fun <T> ItemStack.tag(key: String, type: TagType<T>) =
    tag(namespaceKey(key), type)

fun <T> ItemStack.tag(key: NamespacedKey, type: TagType<T>, value: T?) =
    meta { value?.let { persistentDataContainer[key, type] = it } ?: persistentDataContainer.remove(key) }

fun <T> ItemStack.tag(key: String, type: TagType<T>, value: T?) =
    tag(namespaceKey(key), type, value)

fun <T> ItemStack.tag(key: NamespacedKey, value: T?) = meta {
    value?.let { persistentDataContainer[key, ItemStackTagTypeMatcher.match(it.javaClass)] = it }
        ?: persistentDataContainer.remove(key)
}

fun <T> ItemStack.tag(key: String, value: T?) =
    tag(namespaceKey(key), value)

fun ItemStack.tags(tags: Map<String, Any?>) = tags.forEach(this::tag)

fun ItemStack.tags(vararg tags: Pair<String, Any?>) = tags(tags.toMap())

operator fun <T> ItemStack.get(key: NamespacedKey, type: TagType<T>) =
    tag(key, type)

operator fun <T> ItemStack.get(key: String, type: TagType<T>) =
    tag(key, type)

operator fun <T> ItemStack.set(key: NamespacedKey, type: TagType<T>, value: T?) =
    tag(key, type, value)

operator fun <T> ItemStack.set(key: String, type: TagType<T>, value: T?) =
    tag(key, type, value)

operator fun <T> ItemStack.set(key: NamespacedKey, value: T?) =
    tag(key, value)

operator fun <T> ItemStack.set(key: String, value: T?) =
    tag(key, value)

fun ItemStack.containsTag(key: NamespacedKey, type: TagType<*>) =
    itemMeta?.persistentDataContainer?.has(key, type) == true

fun ItemStack.containsTag(key: String, type: TagType<*>) =
    containsTag(namespaceKey(key), type)

operator fun ItemStack.contains(key: NamespacedKey) =
    itemMeta?.persistentDataContainer?.has(key) == true

operator fun ItemStack.contains(key: String) =
    contains(namespaceKey(key))

operator fun ItemStack.contains(key: Pair<String, TagType<*>>) =
    containsTag(key.first, key.second)
