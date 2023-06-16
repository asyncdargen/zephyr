package dev.zephyr.extensions.bukkit

import net.minecraft.server.MinecraftServer
import org.bukkit.Bukkit

val PluginManager get() = Bukkit.getPluginManager()

val CurrentTick = MinecraftServer.currentTick