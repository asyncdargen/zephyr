package dev.zephyr.util.kotlin

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@KotlinOpens
class ObservableProperty<V>(
    value: V, val passIfEqual: Boolean = true, val observer: (old: V, new: V) -> Unit
) : kotlin.properties.ObservableProperty<V>(value) {

    override fun beforeChange(property: KProperty<*>, oldValue: V, newValue: V): Boolean {
        return passIfEqual || oldValue != newValue
    }

    override fun afterChange(property: KProperty<*>, oldValue: V, newValue: V) {
        observer(oldValue, newValue)
    }

}

fun <V> observable(
    value: V, passIfEqual: Boolean = true,
    observer: (old: V, new: V) -> Unit
): ReadWriteProperty<Any?, V> = ObservableProperty(value, passIfEqual, observer)