package dev.zephyr.protocol.entity.type.sniffer

import net.minecraft.world.entity.animal.sniffer.Sniffer

enum class SnifferState {

    IDLING,
    FEELING_HAPPY,
    SCENTING,
    SNIFFING,
    SEARCHING,
    DIGGING,
    RISING;

    val handle
        get() = Sniffer.a.values()[ordinal]

}