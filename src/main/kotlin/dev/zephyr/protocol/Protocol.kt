package dev.zephyr.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ConnectionSide
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import dev.zephyr.Zephyr

object Protocol {

    val ProtocolManager by lazy { ProtocolLibrary.getProtocolManager() }

    fun onReceive(vararg types: PacketType, async: Boolean = false, handler: PacketEvent.() -> Unit) {
        ProtocolManager.addPacketListener(object : PacketAdapter(
            AdapterParameteters()
                .connectionSide(ConnectionSide.SERVER_SIDE)
                .plugin(Zephyr.Plugin)
                .types(*types)
                .apply { if (async) optionAsync() else optionSync() }
        ) {
            override fun onPacketReceiving(event: PacketEvent) = handler(event)
        })
    }

    fun onSend(vararg types: PacketType, async: Boolean = false, handler: PacketEvent.() -> Unit) {
        ProtocolManager.addPacketListener(object : PacketAdapter(
            AdapterParameteters()
                .connectionSide(ConnectionSide.SERVER_SIDE)
                .plugin(Zephyr.Plugin)
                .types(*types)
                .apply { if (async) optionAsync() else optionSync() }
        ) {
            override fun onPacketSending(event: PacketEvent) = handler(event)
        })
    }

}