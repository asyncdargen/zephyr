package dev.zephyr.protocol.entity.metadata

import com.comphenix.protocol.wrappers.WrappedDataValue
import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
data class MetadataItem<T>(
    @get:JvmName("_index") val index: Int,
    val type: MetadataType<T>,
    @get:JvmName("_value") val value: T?
) : WrappedDataValue(index, type.serializer, value?.let { type.converter?.invoke(it) ?: it }?.let(MetadataType.Companion::unwrap))