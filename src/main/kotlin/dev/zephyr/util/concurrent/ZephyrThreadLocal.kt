package dev.zephyr.util.concurrent

import dev.zephyr.util.kotlin.KotlinOpens
import io.netty.util.concurrent.FastThreadLocal

@KotlinOpens
class ZephyrThreadLocal<V> : FastThreadLocal<V>() {

    fun contains() = ifExists != null

    fun getOrNull(): V? = ifExists

}

@KotlinOpens
class LazyThreadLocal<V>(val initializer: () -> V) : ZephyrThreadLocal<V>() {

    override fun initialValue() = initializer()

}

fun <V> threadLocal() = ZephyrThreadLocal<V>()

fun <V> threadLocal(initializer: () -> V) = LazyThreadLocal(initializer)