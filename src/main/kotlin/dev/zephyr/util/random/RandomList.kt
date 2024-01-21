package dev.zephyr.util.random

interface RandomList<T> : Collection<T> {

    fun takeRandom(): T

    fun takeRandom(n: Int): List<T>

    fun takeRandomCount(): List<T>

}