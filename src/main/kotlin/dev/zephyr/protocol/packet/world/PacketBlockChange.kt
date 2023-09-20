package dev.zephyr.protocol.packet.world

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Material
import org.bukkit.block.data.BlockData

@KotlinOpens
class PacketBlockChange : ProtocolPacket(PacketPlayOutType.BLOCK_CHANGE) {

    var position by writer(0, blockPositionModifier)

    var wrappedData by writer(0, blockData)
    var data by writer<BlockData, WrappedBlockData>(0, blockData, WrappedBlockData::createData)
    var type by writer<Material, WrappedBlockData>(0, blockData, WrappedBlockData::createData)

}