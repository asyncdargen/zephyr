package dev.zephyr.protocol.world.block

import com.comphenix.protocol.wrappers.WrappedBlockData
import dev.zephyr.protocol.world.StructureProtocol
import dev.zephyr.protocol.world.position
import dev.zephyr.util.bukkit.block
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.block.data.BlockData

@KotlinOpens
class SingleProtocolBlock(location: Location, wrappedBlockData: WrappedBlockData) : ProtocolBlock() {
    constructor(location: Location, blockData: BlockData) : this(location, WrappedBlockData.createData(blockData))

    init {
        this.wrappedBlockData = wrappedBlockData
    }

    override val location = location.block()
    override val position = location.position

    fun register() = apply { StructureProtocol.Blocks.add(this) }

    override fun remove() {
        StructureProtocol.Blocks.remove(this)
        super.remove()
    }

}
