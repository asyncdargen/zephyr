package dev.zephyr.protocol.world.batch

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.world.Position
import org.bukkit.entity.Player

interface BlockBatcher {

    fun batch(section: Position, blocks: Map<Position, WrappedBlockData>, flag: Boolean = true, players: Collection<Player>)

}