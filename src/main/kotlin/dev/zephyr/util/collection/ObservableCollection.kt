package dev.zephyr.util.collection

import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class ObservableCollection<E>(
    val collection: MutableCollection<E>,
    val handleAdd: (E) -> Unit,
    val handleRemove: (E) -> Unit
) : MutableCollection<E> by collection {

    override fun add(element: E) =
        collection.add(element).apply { handleAdd(element) }

    override fun addAll(elements: Collection<E>) =
        collection.addAll(elements).apply { forEach(handleAdd) }

    override fun remove(element: E) =
        collection.remove(element).apply { handleRemove(element) }

    override fun removeAll(elements: Collection<E>) =
        collection.removeAll(elements).apply { forEach(handleRemove) }

}