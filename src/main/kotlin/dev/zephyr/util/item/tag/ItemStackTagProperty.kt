package dev.zephyr.util.item.tag

import dev.zephyr.util.bukkit.namespaceKey
import dev.zephyr.util.item.TagType
import dev.zephyr.util.item.get
import dev.zephyr.util.item.set
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
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
class MappableItemStackTagProperty<T, V>(
    key: NamespacedKey, type: TagType<V>,
    val mapper: (T) -> V, val unmapper: (V) -> T
) : ReadWriteProperty<ItemStack, T?> {

    val delegate = tag(key, type)

    override fun getValue(thisRef: ItemStack, property: KProperty<*>): T? {
        return delegate.getValue(thisRef, property)?.let(unmapper)
    }

    override fun setValue(thisRef: ItemStack, property: KProperty<*>, value: T?) {
        delegate.setValue(thisRef, property, value?.let(mapper))
    }

}

@KotlinOpens
class EnumItemStackTagProperty<E : Enum<E>>(
    key: NamespacedKey, val enumType: Class<E>
) : MappableItemStackTagProperty<E, Int>(key, TagType.INTEGER, { it.ordinal }, { enumType.enumConstants[it] })

@KotlinOpens
class UUIDItemStackTagProperty(key: NamespacedKey) :
    MappableItemStackTagProperty<UUID, String>(key, TagType.STRING, UUID::toString, UUID::fromString)

fun <T> tag(key: NamespacedKey, type: TagType<T>) = ItemStackTagProperty(key, type)

fun <T> tag(key: String, type: TagType<T>) = tag(namespaceKey(key), type)

fun <T, V> mappedTag(key: NamespacedKey, type: TagType<V>, mapper: (T) -> V, unmapper: (V) -> T) =
    MappableItemStackTagProperty(key, type, mapper, unmapper)

fun <T, V> mappedTag(key: String, type: TagType<V>, mapper: (T) -> V, unmapper: (V) -> T) =
    mappedTag(namespaceKey(key), type, mapper, unmapper)

inline fun <reified E : Enum<E>> enumTag(key: NamespacedKey) = EnumItemStackTagProperty(key, E::class.java)

inline fun <reified E : Enum<E>> enumTag(key: String) = enumTag<E>(namespaceKey(key))

fun uuidTag(key: NamespacedKey) = UUIDItemStackTagProperty(key)

fun uuidTag(key: String) = uuidTag(namespaceKey(key))
