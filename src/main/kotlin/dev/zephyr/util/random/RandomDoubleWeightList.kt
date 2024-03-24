package dev.zephyr.util.random

import kotlin.random.Random

class RandomDoubleWeightList<T>(private val itemWeight: (T) -> Double, values: Collection<T>) : RandomList<T>,
    Collection<T> by values {

    val weight = map(itemWeight).sum()

    override fun takeRandom(): T {
        var random = Random.nextDouble(weight)
        return filter { value ->
            val weight = itemWeight(value)
            if (random <= weight) return@filter true else random -= weight
            false
        }.first()
    }

    override fun takeRandom(n: Int) = (0..<n).map { takeRandom() }

    override fun takeRandomCount() = takeRandom(Random.nextInt(size + 1))

}

fun <T> Collection<T>.toRandomList(weightMapper: (T) -> Double): RandomList<T> =
    RandomDoubleWeightList(weightMapper, this)