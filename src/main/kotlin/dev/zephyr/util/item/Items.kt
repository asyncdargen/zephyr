@file:Suppress("DEPRECATION")

package dev.zephyr.util.item

import com.google.common.cache.CacheBuilder
import dev.zephyr.util.bukkit.toComponent
import dev.zephyr.util.bukkit.toComponentList
import dev.zephyr.util.kotlin.cast
import dev.zephyr.util.kotlin.safeCast
import dev.zephyr.util.kotlin.unit
import net.kyori.adventure.text.TranslatableComponent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.concurrent.TimeUnit

val MetaCache = CacheBuilder.newBuilder()
    .expireAfterAccess(1, TimeUnit.MINUTES)
    .build<Int, ItemMeta?>()

inline fun item(type: Material, amount: Int = 1, block: ItemStack.() -> Unit = {}) =
    ItemStack(type, amount).apply(block)

inline fun item(item: ItemStack, block: ItemStack.() -> Unit = {}) =
    item.clone().apply(block)

inline fun ItemStack.meta(crossinline block: ItemMeta.() -> Unit = {}) = metaAs<ItemMeta>(block)

inline fun <reified T : ItemMeta> ItemStack.metaAs(crossinline block: T.() -> Unit = {}) =
    apply { meta = metaAs<T>()?.apply(block) }

inline fun <reified T : ItemMeta> ItemStack.metaAs() =
    meta?.safeCast<T>()

inline var ItemStack.meta: ItemMeta?
    get() = itemMeta//MetaCache.asMap().getOrPut(referenceHash, this::getItemMeta)
    set(value) {
        itemMeta = value
//        value?.let { MetaCache.put(referenceHash, it) }
    }

val ItemStack.referenceHash get() = System.identityHashCode(this)

fun ItemStack.asComponent() = displayName().cast<TranslatableComponent>().key("disconnect.genericReason")

var ItemStack.displayName: String?
    set(value) = meta { displayName(value.toComponent()) }.unit()
    get() = meta?.displayName

var ItemStack.stringLore: List<String>
    set(value) = lore(value.toComponentList())
    get() = lore ?: mutableListOf()

fun ItemStack.lore(builder: MutableList<String>.() -> Unit) = meta { stringLore = buildList(builder) }

fun ItemStack.addLore(lore: List<String>) = meta { addLore(lore) }

fun ItemMeta.addLore(vararg lore: String) = addLore(lore.toList())

fun ItemMeta.addLore(lore: List<String>) {
    val components = lore.toComponentList()
    lore(lore()?.apply { addAll(components) } ?: components)
}

fun ItemStack.addLore(vararg lore: String) = addLore(lore.toList())
