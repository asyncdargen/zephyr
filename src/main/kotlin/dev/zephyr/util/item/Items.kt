package dev.zephyr.util.item

import dev.zephyr.util.bukkit.toComponent
import dev.zephyr.util.bukkit.toComponents
import dev.zephyr.util.kotlin.unit
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

inline fun item(type: Material, amount: Int = 1, block: ItemStack.() -> Unit = {}) =
    ItemStack(type, amount).apply(block)

inline fun item(item: ItemStack, block: ItemStack.() -> Unit = {}) =
    item.clone().apply(block)

inline fun <reified T : ItemMeta> ItemStack.metaAs(crossinline block: T.() -> Unit = {}) =
    itemMeta.apply { editMeta(T::class.java) { block(it) } }

inline fun ItemStack.meta(crossinline block: ItemMeta.() -> Unit = {}) = metaAs<ItemMeta>(block)

var ItemStack.displayName: String?
    set(value) = meta { displayName(value?.toComponent()) }.unit()
    get() = displayName().toString()

var ItemStack.stringLore
    set(value) = lore(value?.toComponents())
    get() = lore()?.map(Component::toString)
