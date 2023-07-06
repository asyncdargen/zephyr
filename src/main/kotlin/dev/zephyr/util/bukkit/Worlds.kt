package dev.zephyr.util.bukkit

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

fun world(name: String = "world") = Bukkit.getWorld(name)

fun World.getBlock(x: Number, y: Number, z: Number) = getBlockAt(x.toInt(), y.toInt(), z.toInt())

fun World.at(x: Number, y: Number, z: Number, yaw: Number = 0, pitch: Number = 0) =
    Location(this, x.toDouble(), y.toDouble(), z.toDouble(), yaw.toFloat(), pitch.toFloat())

fun Location.at(
    x: Number = this.x, y: Number = this.y, z: Number = this.z,
    yaw: Number = this.yaw, pitch: Number = this.pitch
) = world.at(x, y, z, yaw, pitch)

fun Location.diff(
    x: Number = 0, y: Number = 0, z: Number = 0,
    yaw: Number = this.yaw, pitch: Number = this.pitch
) = at(
    this.x + x.toDouble(), this.y + y.toDouble(), this.z + z.toDouble(),
    yaw, pitch
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

fun Location.direction(to: Location) = subtract(to).direction

fun Location.clearAngles() = diff(yaw = 0, pitch = 0)