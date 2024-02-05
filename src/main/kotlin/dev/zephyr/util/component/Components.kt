package dev.zephyr.util.component

import com.comphenix.protocol.wrappers.WrappedChatComponent
import dev.zephyr.util.format.colored
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer


fun Component.literal() = LegacyComponentSerializer.legacySection().serialize(this)

fun Iterable<Component>.literal() = map(Component::literal)

fun Any?.asComponent() = this as? Component ?: this?.toString().toComponent()

fun String?.toComponent() = this?.colored()?.let(LegacyComponentSerializer.legacySection()::deserialize) ?: Component.empty()

fun Iterable<String>.toComponents() = map(String::toComponent)

fun String?.miniComponent() = this?.let(MiniMessage.miniMessage()::deserialize) ?: Component.empty()

fun Iterable<String>.miniComponents() = map(String::miniComponent)

fun WrappedChatComponent.unwrap() = GsonComponentSerializer.gson().deserialize(json)

fun WrappedChatComponent.unwrap(fallback: Component) = GsonComponentSerializer.gson().deserializeOr(json, fallback)

fun Component.wrap() = WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(this))