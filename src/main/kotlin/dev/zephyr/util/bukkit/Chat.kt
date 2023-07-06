package dev.zephyr.util.bukkit

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.md_5.bungee.api.chat.TextComponent

fun Collection<String>.toComponentList() = map(String::toComponent)

fun String?.toComponent() = this?.let { component ->
    TextComponent.fromLegacyText(component)
        .asSequence()
        .filterIsInstance<TextComponent>()
        .map {
            Component.text(it.text)
                .color(TextColor.color(it.color.color.rgb))
                .decoration(TextDecoration.BOLD, it.isBold)
                .decoration(TextDecoration.ITALIC, it.isItalic)
                .decoration(TextDecoration.STRIKETHROUGH, it.isStrikethrough)
                .decoration(TextDecoration.UNDERLINED, it.isUnderlined)
                .decoration(TextDecoration.OBFUSCATED, it.isObfuscated)
        }
        .toList()
        .toTypedArray()
        .let(Component::textOfChildren)
} ?: Component.empty()

fun Collection<Component>.toTextList() = map(Component::toText)

fun Component.toText() =
    LegacyComponentSerializer.legacySection().serialize(this)