@file:Suppress("DEPRECATION")

package dev.zephyr.util.item

import dev.zephyr.util.bukkit.toComponent
import dev.zephyr.util.bukkit.toComponentList
import dev.zephyr.util.kotlin.safeCast
import dev.zephyr.util.kotlin.unit
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

inline fun item(type: Material, amount: Int = 1, block: ItemStack.() -> Unit = {}) =
    ItemStack(type, amount).apply(block)

inline fun item(item: ItemStack, block: ItemStack.() -> Unit = {}) =
    item.clone().apply(block)

inline fun <reified T : ItemMeta> ItemStack.metaAs(crossinline block: T.() -> Unit = {}) =
    itemMeta?.safeCast<T>()?.apply(block).apply(this::setItemMeta)

inline fun ItemStack.meta(crossinline block: ItemMeta.() -> Unit = {}) = metaAs<ItemMeta>(block)

var ItemStack.displayName: String?
    set(value) = meta { displayName(value.toComponent()) }.unit()
    get() = itemMeta.displayName

var ItemStack.stringLore: List<String>
    set(value) = lore(value.toComponentList())
    get() = lore ?: mutableListOf()

fun ItemStack.lore(builder: MutableList<String>.() -> Unit) = apply {
    meta { stringLore = buildList(builder) }
}

fun ItemStack.addLore(lore: List<String>) = apply {
    stringLore = stringLore + lore
}