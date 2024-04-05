package dev.zephyr.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.reflect.StructureModifier
import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.util.kotlin.cast
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.craftbukkit.v1_19_R3.block.data.CraftBlockData

typealias PacketPlayOutType = PacketType.Play.Server
typealias PacketPlayInType = PacketType.Play.Client

fun <T> StructureModifier<T>.edit(index: Int, mapper: (T) -> T) = write(index, read(index).run(mapper))

private val WrappedBlockDataPool = hashMapOf<BlockData, WrappedBlockData>()
private val WrappedBlockDataMaterialPool = hashMapOf<Material, WrappedBlockData>()

fun BlockData.wrap() = WrappedBlockDataPool.getOrPut(this) { WrappedBlockData.createData(this) }

fun Material.createWrappedBlockData() = WrappedBlockDataMaterialPool.getOrPut(this) { WrappedBlockData.createData(this) }

fun WrappedBlockData.unwrap() = CraftBlockData.fromData(handle.cast())