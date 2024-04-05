package dev.zephyr.util.bukkit

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.util.kotlin.cast
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.IBlockData
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData

inline fun <reified D : BlockData> Material.createData() = createBlockData().cast<D>()

val BlockData.stateId get() = Block.i(cast<CraftBlockData>().state)

val WrappedBlockData.stateId get() = Block.i(handle as IBlockData)