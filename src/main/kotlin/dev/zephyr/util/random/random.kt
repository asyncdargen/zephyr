package dev.zephyr.util.random


fun java.util.Random.nextSign() = if (nextBoolean()) 1 else -1

fun kotlin.random.Random.nextSign() = if (nextBoolean()) 1 else -1