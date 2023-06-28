package dev.zephyr.util.java

import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodHandles.Lookup
import java.lang.reflect.Field
import java.lang.reflect.Method

val Lookup = MethodHandles.Lookup::class.java
    .getDeclaredField("IMPL_LOOKUP")
    .getObjectWithUnsafe<Lookup>()!!

fun Field.unreflectGetter() = Lookup.unreflectGetter(this)

fun Field.unreflectSetter() = Lookup.unreflectGetter(this)

fun Method.unreflect() = Lookup.unreflect(this)