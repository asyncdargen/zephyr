package dev.zephyr.protocol

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import dev.zephyr.Zephyr

object Protocol {

    val ProtocolManager by lazy { ProtocolLibrary.getProtocolManager() }

    fun onReceive(types: Collection<PacketType>, handler: PacketEvent.() -> Unit) {
        ProtocolManager.addPacketListener(object : PacketAdapter(Zephyr.Plugin, types) {
            override fun onPacketReceiving(event: PacketEvent) = handler(event)
        })
    }

    fun onReceive(types: PacketType, handler: PacketEvent.() -> Unit) = onReceive(listOf(types), handler)

    fun onSend(types: Collection<PacketType>, handler: PacketEvent.() -> Unit) {
        ProtocolManager.addPacketListener(object : PacketAdapter(Zephyr.Plugin, types) {
            override fun onPacketSending(event: PacketEvent) = handler(event)
        })
    }

    fun onSend(types: PacketType, handler: PacketEvent.() -> Unit) = onSend(listOf(types), handler)

}