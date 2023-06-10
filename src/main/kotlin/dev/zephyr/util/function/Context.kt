package dev.zephyr.util.function

interface Context<T : Context<T>> {

    fun close()

}

interface ForkingContext<T : ForkingContext<T>> : Context<T> {

    val forks: MutableSet<T>


    fun fork(follow: Boolean = true): T

}