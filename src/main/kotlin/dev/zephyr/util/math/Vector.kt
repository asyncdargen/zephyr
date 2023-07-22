package dev.zephyr.util.math

import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

fun Vector.rotateY(angle: Double): Vector {
    val sin = sin(angle)
    val cos = cos(angle)

    val x = this.x * cos - this.z * sin
    val z = this.z * cos + this.x * sin

    return setX(x).setZ(z)
}