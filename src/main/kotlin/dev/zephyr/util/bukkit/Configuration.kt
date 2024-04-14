package dev.zephyr.util.bukkit

import dev.zephyr.Zephyr
import dev.zephyr.util.component.miniComponent
import dev.zephyr.util.component.miniComponents
import dev.zephyr.util.item.displayName
import dev.zephyr.util.item.item
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.configuration.Configuration
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
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

fun <T> ConfigurationSection.section(key: String, block: (ConfigurationSection) -> T) =
    section(key)?.let(block)

fun ConfigurationSection.section(key: String) = getConfigurationSection(key)

fun String.toLocation(world: World = Bukkit.getWorld(split(" ")[0])!!): Location {
    val (z, y, x) = split(" ").reversed()
    return Location(world, x.toDouble(), y.toDouble(), z.toDouble())
}

fun ConfigurationSection.getStringLocation(key: String, default: Location? = null) = getString(key)?.toLocation() ?: default

inline fun <reified E : Enum<E>> ConfigurationSection.getEnum(key: String, default: E? = null) =
    getString(key)?.let { enumValueOf<E>(it.uppercase()) } ?: default

fun ConfigurationSection.getShortItemStack(key: String, default: ItemStack? = null): ItemStack? = section(key) {
    item(getEnum<Material>("type")!!) {
        getString("name")?.miniComponent()?.let { displayName = it }
        lore(getStringList("lore").miniComponents())
    }
} ?: default

fun ConfigurationSection.getMiniComponent(key: String) = getString("key").miniComponent()

fun ConfigurationSection.getMiniComponentList(key: String) = getStringList("key").miniComponents()
