package dev.zephyr.extensions.bukkit

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player

fun World.getBlock(x: Number, y: Number, z: Number) = getBlockAt(x.toInt(), y.toInt(), z.toInt())

fun Location.getNearEntities(radius: Number) =
    world.getNearbyEntities(this, radius.toDouble(), radius.toDouble(), radius.toDouble())
        .asSequence()
        .filterIsInstance<LivingEntity>()

fun Location.getNearPlayers(radius: Number) =
    getNearEntities(radius).filterIsInstance<Player>()

infix fun Location.distanceTo(location: Location) =
    if (location.world !== world) Double.MAX_VALUE else distance(location)

fun Location.direction(to: Location) = subtract(to).direction