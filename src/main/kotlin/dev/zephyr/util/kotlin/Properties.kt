package dev.zephyr.util.kotlin

import kotlin.reflect.KProperty0

val KProperty0<*>.isLazyInitialized
    get() = safeCast<Lazy<*>>()?.isInitialized() == true