package dev.zephyr.util.concurrent

import dev.zephyr.util.kotlin.KotlinOpens
import io.netty.util.concurrent.FastThreadLocal

@KotlinOpens
class LazyThreadLocal<V>(val initializer: () -> V) : FastThreadLocal<V>() {

    fun contains() = ifExists != null

    override fun initialValue() = initializer()

}

inline fun <V> threadLocal(noinline initializer: () -> V) = LazyThreadLocal(initializer)