package dev.zephyr.util.bukkit

import dev.zephyr.Zephyr
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

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

fun <T> mapConfig(name: String, block: (Configuration) -> T) = loadConfig(name).run(block)

fun ConfigurationSection.getMaps(section: String) = getList(section)?.filterIsInstance<Map<String, Any>>()

fun ConfigurationSection.forEachSections(action: (String, ConfigurationSection) -> Unit) =
    getKeys(false).forEach { action(it, getConfigurationSection(it)!!) }

fun ConfigurationSection.forEachKeys(action: (String, ConfigurationSection) -> Unit) =
    getKeys(false).forEach { action(it, this) }

fun ConfigurationSection.forEachKeys(section: String, action: (String, ConfigurationSection) -> Unit) =
    getConfigurationSection(section)?.forEachKeys(action)