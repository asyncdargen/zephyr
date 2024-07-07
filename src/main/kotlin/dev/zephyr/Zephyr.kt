package dev.zephyr

import dev.zephyr.hook.player.PlayerHooks
import dev.zephyr.menu.MenuService
import dev.zephyr.protocol.entity.EntityProtocol
import dev.zephyr.protocol.scoreboard.ScoreboardProtocol
import dev.zephyr.protocol.world.PlayerChunks
import dev.zephyr.protocol.world.StructureProtocol
import dev.zephyr.util.java.openJavaModules
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

object Zephyr : Listener {

    lateinit var Plugin: JavaPlugin

    val Logger by lazy { Plugin.logger}
    val DataFolder by lazy { Plugin.dataFolder.toPath() }
    val PluginNamespace by lazy { Plugin.name.lowercase() }

    fun initialize(
        plugin: JavaPlugin = Thread.currentThread().stackTrace
            .firstOrNull { JavaPlugin::class.java.isAssignableFrom(Class.forName(it.className)) }
            ?.let { JavaPlugin.getProvidingPlugin(Class.forName(it.className)) }
            ?: throw IllegalStateException("Not from plugin context")
    ) {
        Plugin = plugin

        openJavaModules()

        PlayerChunks
        EntityProtocol
        ScoreboardProtocol
        StructureProtocol

        MenuService
        PlayerHooks

        repeat(100000) {
            println("52 3 $it")
        }
    }

}