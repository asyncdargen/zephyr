package dev.zephyr.util.item.tag

import dev.zephyr.util.bukkit.namespaceKey
import dev.zephyr.util.item.TagType
import dev.zephyr.util.item.get
import dev.zephyr.util.item.set
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@KotlinOpens
class ItemStackTagProperty<T>(val key: NamespacedKey, val type: TagType<T>) : ReadWriteProperty<ItemStack, T?> {

    override fun getValue(thisRef: ItemStack, property: KProperty<*>): T? {
        return thisRef[key, type]
    }

    override fun setValue(thisRef: ItemStack, property: KProperty<*>, value: T?) {
        thisRef[key, type] = value
    }

}

@KotlinOpens
class MappableItemStackTagProperty<V, T>(
    key: NamespacedKey, type: TagType<T>,
    val codec: ItemStackTagCodec<V, T>
) : ReadWriteProperty<ItemStack, V?> {

    val delegate = tag(key, type)

    override fun getValue(thisRef: ItemStack, property: KProperty<*>): V? {
        return delegate.getValue(thisRef, property)?.let(codec::decode)
    }

    override fun setValue(thisRef: ItemStack, property: KProperty<*>, value: V?) {
        delegate.setValue(thisRef, property, value?.let(codec::encode))
    }

}

fun <T> tag(key: NamespacedKey, type: TagType<T>) = ItemStackTagProperty(key, type)

fun <T> tag(key: String, type: TagType<T>) = tag(namespaceKey(key), type)

fun <V, T> tag(key: NamespacedKey, type: TagType<T>, codec: ItemStackTagCodec<V, T>) =
    MappableItemStackTagProperty(key, type, codec)

fun <V, T> tag(key: String, type: TagType<T>, codec: ItemStackTagCodec<V, T>) =
    tag(namespaceKey(key), type, codec)

fun <V, T> tag(key: NamespacedKey, type: TagType<T>, codecBuilder: ItemStackTagCodecBuilder<V, T>.() -> Unit) =
    MappableItemStackTagProperty(key, type, tagCodec(codecBuilder))

fun <V, T> tag(key: String, type: TagType<T>, codecBuilder: ItemStackTagCodecBuilder<V, T>.() -> Unit) =
    tag(namespaceKey(key), type, codecBuilder)

fun <V, T> tag(key: NamespacedKey, type: TagType<T>, encoder: (V) -> T?, decoder: (T) -> V?) =
    tag(key, type) { encode(encoder); decode(decoder) }

fun <V, T> tag(key: String, type: TagType<T>, encoder: (V) -> T?, decoder: (T) -> V?) =
    tag(namespaceKey(key), type, encoder, decoder)

inline fun <reified E : Enum<E>> enumTag(key: NamespacedKey) =
    tag<E, Int>(key, PersistentDataType.INTEGER, Enum<*>::ordinal, E::class.java.enumConstants::get)

inline fun <reified E : Enum<E>> enumTag(key: String) =
    enumTag<E>(namespaceKey(key))

fun uuidTag(key: NamespacedKey) =
    tag(key, PersistentDataType.STRING, UUID::toString, UUID::fromString)

fun uuidTag(key: String) =
    uuidTag(namespaceKey(key))
