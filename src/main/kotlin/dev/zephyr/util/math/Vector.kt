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

fun Vector.rotateX(angle: Double): Vector {
    val sin = sin(angle)
    val cos = cos(angle)

    val y = this.y * cos - this.z * sin
    val z = this.z * cos + this.y * sin

    return setY(y).setZ(z)
}

fun Vector.rotateZ(angle: Double): Vector {
    val sin = sin(angle)
    val cos = cos(angle)

    val x = this.x * cos - this.y * sin
    val y = this.y * cos + this.x * sin

    return setX(x).setY(y)
}