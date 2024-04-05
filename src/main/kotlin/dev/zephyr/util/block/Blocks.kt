package dev.zephyr.util.block

import com.comphenix.protocol.wrappers.BlockPosition
import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.wrap
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block

val AirBlockData = Material.AIR.createBlockData()
val WrappedAirBlockData = AirBlockData.wrap()

val Location.positionHash get() = positionHash(blockX, blockY, blockZ)
val Block.positionHash get() = positionHash(x, y, z)
val BlockPosition.positionHash get() = positionHash(x, y, z)
val Position.positionHash get() = positionHash(x, y, z)

fun positionHash(x: Int, y: Int, z: Int) =
    ((x.toLong() and 0xFFFFL) shl 32) or
            ((y.toLong() and 0xFFFFL) shl 16) or
            (z.toLong() and 0xFFFFL)