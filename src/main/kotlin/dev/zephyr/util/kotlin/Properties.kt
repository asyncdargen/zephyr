package dev.zephyr.util.kotlin

import java.util.concurrent.CompletableFuture
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

val KProperty0<*>.isLazyInitialized
    get() = safeCast<Lazy<*>>()?.isInitialized() == true

inline fun <T> KMutableProperty0<T>.getOr(block: () -> T) = get() ?: block().apply(::set)

operator fun <T> CompletableFuture<T>.getValue(thisRef: Any?, property0: KProperty<*>) = get()