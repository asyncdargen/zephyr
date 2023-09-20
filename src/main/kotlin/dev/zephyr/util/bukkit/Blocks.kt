package dev.zephyr.util.bukkit

import dev.zephyr.util.kotlin.cast
import net.minecraft.world.level.block.Block
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData

inline fun <reified D : BlockData> Material.createData() = createBlockData().cast<D>()

val BlockData.state get() = Block.i(cast<CraftBlockData>().state)