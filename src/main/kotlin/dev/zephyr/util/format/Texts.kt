package dev.zephyr.util.format

import dev.zephyr.util.numbers.hex
import net.md_5.bungee.api.ChatColor
import java.awt.Color
import kotlin.math.ceil

val HexColorPattern = "§#([a-fA-F0-9]{6})".toRegex()

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

fun String.colored() = HexColorPattern.replace(replace('&', '§')) {
    it.groupValues[1].fold("§x") { result, char -> "$result§$char" }
}

fun Int.hexColor() = hex().run { if (length == 6) this else "0".repeat(6 - length) + this }.hexColor()

fun String.hexColor() = "§x§${toCharArray().joinToString("§")}"

const val SMALL_LETTERS = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘꞯʀꜱᴛᴜᴠᴡxʏᴢ"
const val SMALL_DIGITS = "₀₁₂₃₄₅₆₇₈₉⁰¹²³⁴⁵⁶⁷⁸⁹"

fun String.mini(digit: Boolean = true, upperDigit: Boolean = false) = String(toCharArray().apply {
    forEachIndexed { index, char ->
        set(
            index, when {
                char.isLetter() -> SMALL_LETTERS[char.code - (if (char.isUpperCase()) 65 else 97)]
                digit && char.isDigit() -> SMALL_DIGITS[(char.code - 48) + (if (upperDigit) 10 else 0)]
                else -> char
            }
        )
    }
})

fun Number.mini(upper: Boolean = false) =
    toString().mini(upperDigit = upper)

fun Double.mini(floatingNumbers: Int = 2, upper: Boolean = false) =
    "%.${floatingNumbers}f".format(this).mini(upperDigit = upper)