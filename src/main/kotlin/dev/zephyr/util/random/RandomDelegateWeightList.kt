package dev.zephyr.util.random

import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.cast

@KotlinOpens
class RandomDelegateWeightList<O, T>(val delegate: RandomList<O>, val extractor: (O) -> T) :
    RandomList<T>, Collection<T> by delegate.cast() {

    override val size by delegate::size

    override fun takeRandom() = extractor(delegate.takeRandom())

    override fun takeRandom(n: Int) = delegate.takeRandom(n).map(extractor)

    override fun takeRandomCount() = delegate.takeRandomCount().map(extractor)

}

fun <O, T> RandomList<O>.extract(extractor: (O) -> T): RandomList<T> = RandomDelegateWeightList(this, extractor)