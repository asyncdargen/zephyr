package dev.zephyr.protocol.entity.metadata

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.property.AnyBitMaskMetadataObservableProperty
import dev.zephyr.protocol.entity.metadata.property.BitMaskMetadataObservableProperty
import dev.zephyr.protocol.entity.metadata.property.BooleanBitMaskMetadataObservableProperty
import dev.zephyr.protocol.entity.metadata.property.MetadataObservableProperty
import dev.zephyr.util.concurrent.threadLocal
import dev.zephyr.util.java.throwIfNonNull
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.safeCast
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap

@KotlinOpens
class Metadata {

    val itemsMap: MutableMap<Int, MetadataItem<*>> = Int2ObjectOpenHashMap()
    val items by itemsMap::values

    var writeDefaults = false

    val bitMasksProperties: MutableMap<Int, MutableSet<BitMaskMetadataObservableProperty<*>>> =
        Int2ObjectOpenHashMap()

    fun writeDefaults() = apply { writeDefaults = true }

    fun updateBitMaskProperty(property: BitMaskMetadataObservableProperty<*>) {
        bitMasksProperties.getOrPut(property.index, ::mutableSetOf).add(property)

        this[property.index, MetadataType.Byte] = bitMasksProperties[property.index]!!
            .filter(BitMaskMetadataObservableProperty<*>::enabled)
            .sumOf(BitMaskMetadataObservableProperty<*>::mask)
            .toByte()
    }

    operator fun set(index: Int, item: MetadataItem<*>) {
        itemsMap[index] = item
    }

    fun set(item: MetadataItem<*>) = set(item.index, item)

    operator fun <T> set(index: Int, type: MetadataType<T>, data: T?) = set(index, type.newItem(index, data))

    operator fun <T> get(index: Int) = itemsMap[index].safeCast<MetadataItem<T>>()

    fun remove(index: Int) {
        itemsMap.remove(index)
    }

    fun clear() = itemsMap.clear()

    fun <T> item(
        index: Int, type: MetadataType<T>, value: T
    ) = item(index, type, value) { it }

    fun <T, V> item(
        index: Int, type: MetadataType<V>, value: T, mapper: (T) -> V
    ) = MetadataObservableProperty(this, index, type, value, mapper)

    fun bitMask(
        index: Int, mask: Int, enabled: Boolean = false
    ) = BooleanBitMaskMetadataObservableProperty(this, index, mask, enabled)

    fun <T> bitMask(
        index: Int, value: T, maskMapper: (T) -> Int
    ) = AnyBitMaskMetadataObservableProperty(this, index, maskMapper, value)

}

@KotlinOpens
class ObservableMetadata(val observer: (Metadata) -> Unit) : Metadata() {

    val contexts = threadLocal { ObservableMetadataContext() }

    override operator fun set(index: Int, item: MetadataItem<*>) {
        if (isInContext()) {
            currentContext()?.items?.set(index, item)
        } else {
            super.set(index, item)

            observer(this)
        }
    }

    override fun remove(index: Int) {
        if (isInContext()) {
            currentContext()?.items?.remove(index)
        } else {
            super.remove(index)

            observer(this)
        }
    }

    override fun clear() {
        if (isInContext()) {
            currentContext()?.items?.clear()
        } else {
            super.clear()

            observer(this)
        }
    }

    fun modify(observable: Boolean = true, batch: Boolean = true, block: (ObservableMetadata) -> Unit): ObservableMetadataContext {
        val new = !isInContext()

        val context = currentContext()
        context.batch = batch

        val throwable = runCatching(block).exceptionOrNull()

        if (new) {
            contexts.remove()

            if (context.batch) {
                context.items.let(itemsMap::putAll)

                if (observable) {
                    observer(this)
                }
            }

            throwable.throwIfNonNull()
        }

        return context
    }

    fun isBatchingContext() = !isInContext() || currentContext().batch

    fun currentContext() = contexts.get()

    fun isInContext() = contexts.contains()

    @KotlinOpens
    class ObservableMetadataContext(
        var batch: Boolean = true,
        val items: MutableMap<Int, MetadataItem<*>> = hashMapOf(),
    )

    companion object {

        //damn, cheating for the fucking kotlin compiler and its recursion bug
        var create: (ProtocolEntity) -> ObservableMetadata =
            { entity -> ObservableMetadata { if (entity.viewers.isNotEmpty()) entity.sendMetadata() } }

    }

}

