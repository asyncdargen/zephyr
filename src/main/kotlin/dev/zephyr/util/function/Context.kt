package dev.zephyr.util.function

interface Context {

    fun close()

}

interface ForkingContext<T : ForkingContext<T>> : Context {

    val forks: MutableSet<T>

    fun fork(follow: Boolean = true): T

}