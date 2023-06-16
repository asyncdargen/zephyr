package dev.zephyr.extensions.java

fun generateGradient(startColor: Int, endColor: Int, steps: Int, skip: Int = 0): List<Int> {
    var skip = skip
    val steps = skip + steps

    val startRed = (startColor shr 16) and 0xFF
    val startGreen = (startColor shr 8) and 0xFF
    val startBlue = startColor and 0xFF

    val endRed = (endColor shr 16) and 0xFF
    val endGreen = (endColor shr 8) and 0xFF
    val endBlue = endColor and 0xFF

    val redStep = (endRed - startRed) / (steps - 1)
    val greenStep = (endGreen - startGreen) / (steps - 1)
    val blueStep = (endBlue - startBlue) / (steps - 1)

    val gradientColors = mutableListOf<Int>()

    for (i in 0 until steps) {
        if (skip-- > 0) continue

        val currentRed = startRed + (redStep * i)
        val currentGreen = startGreen + (greenStep * i)
        val currentBlue = startBlue + (blueStep * i)

        val color = (currentRed shl 16) or (currentGreen shl 8) or currentBlue
        gradientColors.add(color)
    }

    return gradientColors
}