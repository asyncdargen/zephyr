package dev.zephyr.util.format

import net.md_5.bungee.api.ChatColor
import java.awt.Color

fun createGradient(startColor: Int, endColor: Int, stepsParam: Int, skipParam: Int = 0): List<Int> {
    var skip = skipParam
    val steps = skip + stepsParam

    val startRed = (startColor shr 16) and 0xFF
    val startGreen = (startColor shr 8) and 0xFF
    val startBlue = startColor and 0xFF

    val endRed = (endColor shr 16) and 0xFF
    val endGreen = (endColor shr 8) and 0xFF
    val endBlue = endColor and 0xFF

    val redStep = (endRed - startRed) / (steps - 1)
    val greenStep = (endGreen - startGreen) / (steps - 1)
    val blueStep = (endBlue - startBlue) / (steps - 1)

    return buildList {
        for (i in 0 until steps) {
            if (skip-- > 0) continue

            val currentRed = startRed + (redStep * i)
            val currentGreen = startGreen + (greenStep * i)
            val currentBlue = startBlue + (blueStep * i)

            val color = (currentRed shl 16) or (currentGreen shl 8) or currentBlue
            add(color)
        }
    }
}

fun createGradient(startColor: String, endColor: String, steps: Int, skip: Int = 0) =
    createGradient(startColor.toInt(16), endColor.toInt(16), steps, skip)

fun createGradient(startColor: Color, endColor: Color, steps: Int, skip: Int = 0) =
    createGradient(startColor.rgb, endColor.rgb, steps, skip)

fun createGradient(startColor: org.bukkit.Color, endColor: org.bukkit.Color, steps: Int, skip: Int = 0) =
    createGradient(startColor.asRGB(), endColor.asRGB(), steps, skip)

fun createGradient(startColor: ChatColor, endColor: ChatColor, steps: Int, skip: Int = 0) =
    createGradient(startColor.color, endColor.color, steps, skip)

