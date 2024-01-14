package dev.zephyr.util.bukkit

import dev.zephyr.util.component.literal
import dev.zephyr.util.component.toComponents
import dev.zephyr.util.component.wrap
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.md_5.bungee.api.chat.TextComponent


@Deprecated("use Iterable#toComponents insead of")
fun Collection<String>.toComponentList() = toComponents()


@Deprecated("use String?#toComponent insead of")
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


@Deprecated("use Component#wrap insead of")
fun Component.toWrappedChatComponent() = wrap()

@Deprecated("use Iterable#literal insead of")
fun Collection<Component>.toTextList() = literal()

@Deprecated("use Component#literal insead of")
fun Component.toText() = literal()