package dev.zephyr.util.bukkit

import dev.zephyr.Zephyr
import org.bukkit.NamespacedKey
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

fun namespaceKey(key: String, namespace: String = Zephyr.PluginNamespace) = NamespacedKey(namespace.lowercase(), key.lowercase())

fun loadConfig(name: String, block: ((Configuration) -> Unit)? = null): Configuration = Zephyr.Plugin.run {
    val configFile = File(dataFolder, name)
    val config = YamlConfiguration()

    if (!configFile.exists()) {
        getResource(name)?.let(::InputStreamReader)?.let(config::load)
        config.save(configFile)
    } else config.load(configFile)

    block?.invoke(config)

    config
}