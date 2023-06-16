package dev.zephyr.extensions.bukkit

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Location
import org.bukkit.entity.Player

fun Player.sendOverlay(message: String) = spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(message))

fun Player.teleportWithoutRotation(destination: Location) =
    teleport(destination.clone().apply { direction = location.direction })