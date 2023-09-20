package dev.zephyr.util.math

import org.bukkit.Location
import org.bukkit.util.Vector
import org.joml.Vector3f
import java.lang.Math.toDegrees
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun Vector3f.subtract(vec: Vector3f) = subtract(vec.x, vec.y, vec.z)

fun Vector3f.subtract(vec: Location) = subtract(vec.x.toFloat(), vec.y.toFloat(), vec.z.toFloat())

fun Vector3f.subtract(dx: Float, dy: Float, dz: Float): Vector3f {
    x -= dx
    y -= dy
    z -= dz
    return this
}

val Vector3f.yaw get() = toDegrees(atan2(-x, z).toDouble())

val Vector3f.pitch get() = toDegrees(atan2(-y, sqrt(x * x + z * z)).toDouble())

val Vector3f.isZero get() = x == 0F && y == 0F && z == 0F

val Vector3f.isNan get() = x.isNaN() || y.isNaN() || z.isNaN()

fun Vector3f.setX(x: Float) = apply { this.x = x }

fun Vector3f.setY(y: Float) = apply { this.y = y }

fun Vector3f.setZ(z: Float) = apply { this.z = z }

fun Vector3f.copy() = Vector3f(x, y, z)

fun Vector.copy(vec: Vector3f) {
    x = vec.x.toDouble()
    y = vec.y.toDouble()
    z = vec.z.toDouble()
}

operator fun Vector3f.times(value: Float): Vector3f = mul(value)

operator fun Vector3f.plus(v3: Vector3f): Vector3f = add(v3)

operator fun Vector3f.minus(v3: Vector3f): Vector3f = subtract(v3)

operator fun Vector3f.unaryMinus(): Vector3f = mul(-1f)

operator fun Vector3f.div(v3: Vector3f) = div(v3)

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

fun Vector.moveAsDirection(yaw: Float, pitch: Float, distance: Float): Vector {
    val direction = directionOf(yaw, pitch)
    direction.normalize().multiply(-distance)
    return clone().add(direction)
}

fun Vector.moveAsVector(vector: Vector3f, distance: Float): Vector {
    val yawPitch = vector.toYawPitch()
    val direction = directionOf(yawPitch.first, yawPitch.second)
    direction.normalize().multiply(distance)
    return clone().add(direction)
}


fun directionOf(yaw: Float, pitch: Float): Vector {
    val yawRadians = Math.toRadians(yaw.toDouble()).toFloat()
    val pitchRadians = Math.toRadians(pitch.toDouble()).toFloat()

    val x = sin(yawRadians) * cos(pitchRadians)
    val y = sin(pitchRadians)
    val z = -cos(yawRadians) * cos(pitchRadians)

    return Vector(x, y, z).normalize()
}

fun Vector3f.toYawPitch(): Pair<Float, Float> {
    val yaw = toDegrees(atan2(-x, z).toDouble()).toFloat()
    val pitch = toDegrees(atan2(-y, sqrt(x * x + z * z)).toDouble()).toFloat()
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