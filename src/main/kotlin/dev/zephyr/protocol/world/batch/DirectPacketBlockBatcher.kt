package dev.zephyr.protocol.world.batch

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.NativeProtocol.sendForcePacket
import dev.zephyr.protocol.world.Position
import dev.zephyr.protocol.writeVarInt
import dev.zephyr.protocol.writeVarLong
import dev.zephyr.util.bukkit.stateId
import io.netty.buffer.Unpooled
import net.minecraft.core.SectionPosition
import org.bukkit.entity.Player

data object DirectPacketBlockBatcher : BlockBatcher {

    override fun batch(
        section: Position,
        blocks: Map<Position, WrappedBlockData>,
        flag: Boolean,
        players: Collection<Player>
    ) {
        val packet = Unpooled.buffer()
        packet.writeVarInt(67)
        packet.writeLong(SectionPosition.b(section.x, section.y, section.z))
        packet.writeBoolean(flag)

        packet.writeVarInt(blocks.size)
        blocks.forEach { (position, block) ->
            val localX = position.x - section.x * 16
            val localY = position.y - section.y * 16
            val localZ = position.z - section.z * 16
            packet.writeVarLong((block.stateId shl 12).toLong() or (localX shl 8 or (localZ shl 4) or localY).toLong())
        }
        players.forEach { it.sendForcePacket(packet) }
    }

}