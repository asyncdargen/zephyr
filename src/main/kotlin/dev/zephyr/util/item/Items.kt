@file:Suppress("DEPRECATION")

package dev.zephyr.util.item

import dev.zephyr.util.bukkit.toComponent
import dev.zephyr.util.bukkit.toComponentList
import dev.zephyr.util.kotlin.cast
import dev.zephyr.util.kotlin.safeCast
import dev.zephyr.util.kotlin.unit
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TranslatableComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

inline fun item(type: Material, amount: Int = 1, block: ItemStack.() -> Unit = {}) =
    ItemStack(type, amount).apply(block)

inline fun item(item: ItemStack, block: ItemStack.() -> Unit = {}) =
    item.clone().apply(block)

inline fun ItemStack.meta(crossinline block: ItemMeta.() -> Unit = {}) = metaAs<ItemMeta>(block)

inline fun <reified T : ItemMeta> ItemStack.metaAs(crossinline block: T.() -> Unit = {}) =
    apply { itemMeta = metaAs<T>()?.apply(block) }

inline fun <reified T : ItemMeta> ItemStack.metaAs() =
    itemMeta?.safeCast<T>()

fun ItemStack.asItemComponent() = displayName().cast<TranslatableComponent>().key("disconnect.genericReason")

var ItemStack.customModel: Int?
    set(value) = meta { setCustomModelData(value) }.unit()
    get() = itemMeta.customModelData

var ItemStack.displayName: Component
    set(value) = meta { displayName(value) }.unit()
    get() = displayName()

var ItemStack.displayNameString: String?
    set(value) = meta { displayName(value.toComponent()) }.unit()
    get() = itemMeta?.displayName

var ItemStack.loreString: List<String>
    set(value) = lore(value.toComponentList())
    get() = lore ?: mutableListOf()

fun ItemStack.lore(builder: MutableList<Component>.() -> Unit) = meta { lore(buildList(builder)) }

fun ItemStack.addLore(lore: List<Component>) = meta { addLore(lore) }

fun ItemMeta.addLore(vararg lore: Component) = addLore(lore.toList())

fun ItemMeta.addLore(lore: List<Component>) = lore(lore()?.apply { addAll(lore) } ?: lore)

fun ItemStack.addLore(vararg lore: Component) = addLore(lore.toList())
