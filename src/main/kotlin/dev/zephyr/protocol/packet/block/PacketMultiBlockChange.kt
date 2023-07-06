package dev.zephyr.protocol.packet.block

import com.comphenix.protocol.wrappers.BlockPosition
import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.world.ChunkSection
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location

@KotlinOpens
class PacketMultiBlockChange : ProtocolPacket(PacketPlayOutType.MULTI_BLOCK_CHANGE) {

    var section by writer(0, sectionPositions)
    var sectionLocation by writer<Location> {
        section = BlockPosition(it.blockX shr 4, it.blockY shr 4, it.blockZ shr 4)
    }
    var chunkSection by writer<ChunkSection> { section = BlockPosition(it.x, it.y, it.z) }

    var blockPositions by writer<Collection<BlockPosition>, ShortArray>(0, shortArrays) {
        it.map { (((it.x and 0xF) shl 8) or ((it.z and 0xF) shl 4) or (it.y and 0xF) shl 0).toShort() }
            .toShortArray()
    }
    var blockPositionsLocations by writer<Collection<Location>, ShortArray>(0, shortArrays) {
        it.map { (((it.blockX and 0xF) shl 8) or ((it.blockY and 0xF) shl 4) or (it.blockZ and 0xF) shl 0).toShort() }
            .toShortArray()
    }

    var blocksDatas by writer<Collection<WrappedBlockData>, Array<WrappedBlockData>>(
        0, blockDataArrays,
        Collection<WrappedBlockData>::toTypedArray
    )

    var blocksByPositions by writer<Map<BlockPosition, WrappedBlockData>> {
        blockPositions = it.keys
        blocksDatas = it.values
    }
    var blocksByLocations by writer<Map<Location, WrappedBlockData>> {
        blockPositionsLocations = it.keys
        blocksDatas = it.values
    }



    var flag by writer(0, booleans)

}