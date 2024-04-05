package dev.zephyr.protocol.world.batch

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.packet.world.PacketMultiBlockChange
import dev.zephyr.protocol.world.Position
import org.bukkit.entity.Player

data object DefaultBlockBatcher : BlockBatcher {

    override fun batch(
        section: Position, blocks: Map<Position, WrappedBlockData>,
        flag: Boolean, players: Collection<Player>
    ) = PacketMultiBlockChange().also {
        it.setMeta("protocol", true)
        it.chunkSection = section
        it.flag = flag
        it.blocksByPositions = blocks.mapKeys { it.key.blockPos }
    }.send(players)


}