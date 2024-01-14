package dev.zephyr.util.component

import com.comphenix.protocol.wrappers.WrappedChatComponent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

fun Component.literal() = LegacyComponentSerializer.legacySection().serialize(this)

fun Iterable<Component>.literal() = map(Component::literal)

fun String?.toComponent() = this?.let(LegacyComponentSerializer.legacySection()::deserialize) ?: Component.empty()

fun Iterable<String>.toComponents() = map(String::toComponent)


fun WrappedChatComponent.unwrap() = GsonComponentSerializer.gson().deserialize(json)

fun WrappedChatComponent.unwrap(fallback: Component) = GsonComponentSerializer.gson().deserializeOr(json, fallback)

fun Component.wrap() = WrappedChatComponent.fromJson(GsonComponentSerializer.gson().serialize(this))