package dev.zephyr.util.collection

import dev.zephyr.util.kotlin.KotlinOpens
import java.util.*

@KotlinOpens
class SwitchableList<E>(collection: Collection<E>) : LinkedList<E>(collection) {
    constructor(array: Array<out E>) : this(array.toList())
    constructor() : this(emptyList())

    fun switch(): E = synchronized(this) {
        removeFirst().apply { addLast(this) }
    }

}