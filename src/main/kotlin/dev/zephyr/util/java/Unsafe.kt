@file:Suppress("UNCHECKED_CAST")

package dev.zephyr.util.java

import sun.misc.Unsafe
import java.lang.reflect.Field
import kotlin.reflect.KClass

val Unsafe = Unsafe::class.java
    .getDeclaredField("theUnsafe")
    .tryAccessAndGet<Unsafe>()!!

fun <T> allocate(type: Class<T>) = Unsafe.allocateInstance(type) as T

fun <T : Any> allocate(type: KClass<T>) = allocate(type.java)

fun <T> Field.getObjectWithUnsafe(instance: Any? = null): T? =
    (takeIf(Field::isStatic)?.let { Unsafe.getObject(Unsafe.staticFieldBase(this), Unsafe.staticFieldOffset(this)) }
        ?: Unsafe.getObject(instance, Unsafe.objectFieldOffset(this))) as? T