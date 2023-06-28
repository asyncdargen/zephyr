package dev.zephyr.protocol.world

import dev.zephyr.protocol.ProtocolObject
import org.bukkit.World
import org.bukkit.entity.Player

class ProtocolStructure(val world: World) : ProtocolObject() {

    override fun sendSpawnPackets(players: Collection<Player>) {
        TODO("Not yet implemented")
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        TODO("Not yet implemented")
    }
    
}