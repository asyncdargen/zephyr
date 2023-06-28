@file:Suppress("UNCHECKED_CAST")

package dev.zephyr.util.java

import java.lang.reflect.Field
import java.lang.reflect.Modifier

fun <T> Field.tryAccessAndGet(instance: Any? = null) =
    takeIf(Field::trySetAccessible)?.get(instance) as T?

fun Field.isStatic() = Modifier.isStatic(modifiers)

fun <T> classOrNull(name: String) = runCatching { Class.forName(name) }.getOrNull() as Class<T>?