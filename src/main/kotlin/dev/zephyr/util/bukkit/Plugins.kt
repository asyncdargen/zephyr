package dev.zephyr.util.bukkit

import dev.zephyr.Zephyr
import org.bukkit.NamespacedKey

fun namespaceKey(key: String, namespace: String = Zephyr.PluginNamespace) = NamespacedKey(namespace.lowercase(), key.lowercase())