package dev.zephyr.extensions.math

import org.bukkit.util.Vector
import org.joml.Vector3f
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun Vector3f.rotatedX(radius: Float, angle: Float): Vector3f {
    val newY = radius * cos(angle)
    val newZ = -radius * sin(angle)
    return Vector3f(x, newY, newZ)
}

fun Vector3f.rotatedY(radius: Float, angle: Float): Vector3f {
    val newX = radius * cos(angle)
    val newZ = radius * sin(angle)
    return Vector3f(newX, y, newZ)
}

fun Vector3f.rotatedZ(radius: Float, angle: Float): Vector3f {
    val newX = radius * cos(angle)
    val newY = radius * sin(angle)
    return Vector3f(newX, newY, z)
}

fun Vector3f.moveAsDirection(yaw: Float, pitch: Float, distance: Float): Vector3f {
    val direction = directionOf(yaw, pitch)
    direction.normalize().mul(-distance)
    return copy().add(direction)
}

fun Vector3f.moveAsVector(vector: Vector3f, distance: Float): Vector3f {
    val yawPitch = vector.toYawPitch()
    val direction = directionOf(yawPitch.first, yawPitch.second)
    direction.normalize().mul(distance)
    return copy().add(direction)
}


fun directionOf(yaw: Float, pitch: Float): Vector3f {
    val yawRadians = Math.toRadians(yaw.toDouble()).toFloat()
    val pitchRadians = Math.toRadians(pitch.toDouble()).toFloat()

    val x = sin(yawRadians) * cos(pitchRadians)
    val y = sin(pitchRadians)
    val z = -cos(yawRadians) * cos(pitchRadians)

    return Vector3f(x, y, z)
}

fun Vector3f.toYawPitch(): Pair<Float, Float> {
    val yaw = Math.toDegrees(atan2(x, -z).toDouble()).toFloat()
    val pitch = Math.toDegrees(atan2(y, sqrt(x * x + z * z)).toDouble()).toFloat()
    return Pair(yaw, pitch)
}

fun Vector3f.multiply(value: Float): Vector3f {
    x *= value
    y *= value
    z *= value
    return this
}

fun Vector.asVector3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

fun Vector3f.asBukkit() = Vector(x, y, z)

fun Vector3f.copy() = Vector3f(x, y, z)