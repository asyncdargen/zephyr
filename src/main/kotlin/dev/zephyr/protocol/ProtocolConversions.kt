package dev.zephyr.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.reflect.StructureModifier
import com.comphenix.protocol.wrappers.BlockPosition
import org.bukkit.Location

typealias PacketPlayOutType = PacketType.Play.Server
typealias PacketPlayInType = PacketType.Play.Client

fun <T> StructureModifier<T>.edit(index: Int, mapper: (T) -> T) = write(index, read(index).run(mapper))

fun Location.asProtocolBlockPosition() = BlockPosition(blockX, blockY, blockZ)