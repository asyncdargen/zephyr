package dev.zephyr.util.math

import org.bukkit.Location
import org.bukkit.util.BoundingBox

fun BoundingBox.shiftCentral(location: Location) =
    shift(location.x - widthX / 2.0, location.y, location.z - widthZ / 2.0)

fun BoundingBox.intersects(box: BoundingBox) =
    (minX >= box.minX && minX <= box.maxX || maxX >= box.minX && maxX <= box.maxX) &&
    (minY >= box.minY && minY <= box.maxY || maxY >= box.minY && maxY <= box.maxY) &&
    (minZ >= box.minZ && minZ <= box.maxZ || maxZ >= box.minZ && maxZ <= box.maxZ)

fun BoundingBox.distanceToMaxFromMinY(box: BoundingBox) =
    maxY - box.minY

fun BoundingBox.distanceToMinFromMinY(box: BoundingBox) =
    minY - box.minY
