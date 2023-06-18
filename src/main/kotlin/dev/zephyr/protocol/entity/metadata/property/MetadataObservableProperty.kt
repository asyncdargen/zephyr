package dev.zephyr.protocol.entity.metadata.property

import dev.zephyr.protocol.entity.metadata.Metadata
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.metadata.ObservableMetadata
import dev.zephyr.util.kotlin.KotlinOpens
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//todo: make more pretty ;d

internal val Metadata.isMutable
    get() = this !is ObservableMetadata || !isInContext() || currentContext().batch

//more better then kotlin.ObserverProperty
interface ObservableProperty<T> : ReadWriteProperty<Any, T> {

    fun update(value: T)

}

@KotlinOpens
class MetadataObservableProperty<T, V>(
    val metadata: Metadata,
    val index: Int, val type: MetadataType<V>, var value: T,
    val mapper: (T) -> V
) : ObservableProperty<T> {

    init {
        if (metadata.writeDefaults)
            update(value)
    }

    lateinit var updateHandler: (MetadataObservableProperty<T, V>) -> Unit

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        update(value)

        if (this::updateHandler.isInitialized) {
            updateHandler(this)
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = value

    override fun update(value: T) {
        if (metadata.isMutable)
            this.value = value

        metadata[index, type] = mapper.invoke(value)
    }

    fun defaults() = apply { update(value) }

    infix fun on(updateHandler: (MetadataObservableProperty<T, V>) -> Unit) =
        apply { this.updateHandler = updateHandler }

}

abstract class BitMaskMetadataObservableProperty<T>(
    val metadata: Metadata, val index: Int, var value: T
) : ObservableProperty<T> {

    abstract val enabled: Boolean
    abstract val mask: Int

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        update(value)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = value

    override fun update(value: T) {
        if (metadata.isMutable)
            this.value = value

        metadata.updateBitMaskProperty(this)
    }

    fun defaults() = apply { update(value) }

}

@KotlinOpens
class BooleanBitMaskMetadataObservableProperty(
    metadata: Metadata,
    index: Int, val enabledMask: Int, enabled: Boolean
) : BitMaskMetadataObservableProperty<Boolean>(
    metadata, index, enabled
) {

    init {
        if (metadata.writeDefaults)
            update(enabled)
    }

    override val enabled
        get() = value

    override val mask
        get() = enabledMask.takeIf { enabled } ?: 0

}

@KotlinOpens
class AnyBitMaskMetadataObservableProperty<T>(
    metadata: Metadata,
    index: Int, val maskMapper: (T) -> Int, value: T
) : BitMaskMetadataObservableProperty<T>(metadata, index, value) {

    init {
        if (metadata.writeDefaults)
            update(value)
    }

    override val enabled
        get() = true

    override val mask
        get() = maskMapper(value)

}

