package dev.zephyr.util.bukkit

import dev.zephyr.util.kotlin.cast
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player


fun world(name: String = "world") = Bukkit.getWorld(name)

fun World.craft() = cast<CraftWorld>()

fun World.getBlock(x: Number, y: Number, z: Number) = getBlockAt(x.toInt(), y.toInt(), z.toInt())

fun World.at(x: Number, y: Number, z: Number, yaw: Number = 0, pitch: Number = 0) =
    Location(this, x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())

fun Location.at(
    x: Number = this.x, y: Number = this.y, z: Number = this.z,
    yaw: Number = this.yaw, pitch: Number = this.pitch
) = world.at(x, y, z, yaw, pitch)

fun Location.diff(
    x: Number = 0, y: Number = 0, z: Number = 0,
    yaw: Number = 0, pitch: Number = 0
) = at(
    this.x + x.toDouble(), this.y + y.toDouble(), this.z + z.toDouble(),
    this.yaw + yaw.toFloat(), this.pitch + pitch.toFloat()
)

fun Location.block() = at(x.toInt(), y.toInt(), z.toInt())

fun Location.getNearEntities(radius: Number) =
    world.getNearbyEntities(this, radius.toDouble(), radius.toDouble(), radius.toDouble())
        .asSequence()
        .filterIsInstance<LivingEntity>()

fun Location.getNearPlayers(radius: Number) =
    getNearEntities(radius).filterIsInstance<Player>()

infix fun Location.distanceTo(location: Location) =
    if (location.world !== world) Double.MAX_VALUE else distance(location)

fun Location.copyDirection(location: Location) = clone().apply { direction = location.direction }

fun Location.directionTo(to: Location) = to.clone().subtract(this).toVector()

fun Location.directedTo(to: Location) = clone().apply { direction = directionTo(to).normalize() }

fun Location.expandByDirection(directionMultiplier: Double = 1.0) =
    clone().add(direction.normalize().multiply(directionMultiplier))

fun Location.expandByDirection(to: Location, directionMultiplier: Double = 1.0) =
    clone().add(directionTo(to).normalize().multiply(directionMultiplier))

fun Location.clearAngles() = at(yaw = 0, pitch = 0)