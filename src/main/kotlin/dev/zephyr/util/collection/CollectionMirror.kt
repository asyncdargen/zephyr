package dev.zephyr.util.collection

import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class CollectionMirror<V, T>(
    val collection: MutableCollection<V>,
    val extractor: (V) -> T, val mapper: (T) -> V
) : MutableCollection<T> {

    override val size get() = collection.size

    override fun clear() = collection.clear()

    override fun addAll(elements: Collection<T>) = collection.addAll(elements.map(mapper))

    override fun add(element: T) = collection.add(mapper(element))

    override fun isEmpty() = collection.isEmpty()

    override fun iterator() = object : MutableIterator<T> {

        val iterator = collection.iterator()

        override fun hasNext() = iterator.hasNext()

        override fun next() = extractor(iterator.next())

        override fun remove() = iterator.remove()

    }

    override fun retainAll(elements: Collection<T>) = collection.retainAll(elements.map(mapper))

    override fun removeAll(elements: Collection<T>) = collection.removeAll(elements.map(mapper))

    override fun remove(element: T) = collection.remove(mapper(element))

    override fun containsAll(elements: Collection<T>) = collection.containsAll(elements.map(mapper))

    override fun contains(element: T) = collection.contains(mapper(element))


}