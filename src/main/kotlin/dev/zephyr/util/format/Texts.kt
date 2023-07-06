package dev.zephyr.util.format

import dev.zephyr.util.java.replaceBy
import dev.zephyr.util.numbers.hex
import net.md_5.bungee.api.ChatColor
import java.awt.Color
import java.util.regex.Pattern
import kotlin.math.ceil

val HexColorPattern = Pattern.compile("§#([a-fA-F0-9]{6})")

fun String.gradient(vararg colors: Int, style: String = "", first: Boolean = true): String {
    val style = style.colored()
    val chunkSize = ceil((length / (colors.size - 1).toDouble())).toInt()

    return if (chunkSize == length) {
        createGradient(colors[0], colors[1], length, if (first) 0 else 1).joinToStringIndexed("") { index, color ->
            "${color.hexColor()}$style${this[index]}"
        }
    } else chunked(chunkSize).joinToStringIndexed("") { index, chunk ->
        chunk.gradient(colors[index], colors[index + 1], style = style, first = index == 0)
    }
}

fun String.gradient(vararg hexColors: String, style: String = "") =
    gradient(*hexColors.map { toInt(16) }.toIntArray(), style = style)

fun String.gradient(vararg colors: Color, style: String = "") =
    gradient(*colors.map(Color::getRGB).toIntArray(), style = style)

fun String.gradient(vararg colors: org.bukkit.Color, style: String = "") =
    gradient(*colors.map(org.bukkit.Color::asRGB).toIntArray(), style = style)

fun String.gradient(vararg chatColors: ChatColor, style: String = "") =
    gradient(*chatColors.map(ChatColor::getColor).toTypedArray(), style = style)

fun String?.colored(): String {
    var input = this ?: return "null"

    input = input.replace('&', '§')

    return input.replaceBy(HexColorPattern) { it.group(1).hexColor() }
}

fun Int.hexColor() = hex().run { if (length == 6) this else "0".repeat(6 - length) + this }.hexColor()

fun String.hexColor() = "§x§${toCharArray().joinToString("§")}"