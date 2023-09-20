package dev.zephyr.util.item.tag

import dev.zephyr.util.kotlin.KotlinOpens

interface ItemStackTagCodec<V, T> {

    fun encode(value: V): T?

    fun decode(encoded: T): V?

}

@KotlinOpens
class ItemStackTagCodecBuilder<V, T> {

    private lateinit var encoder: (V) -> T?
    private lateinit var decoder: (T) -> V?

    fun encode(encoder: (V) -> T?) = apply { this.encoder = encoder }

    fun decode(decoder: (T) -> V?) = apply { this.decoder = decoder }

    fun create() = object : ItemStackTagCodec<V, T> {

        override fun encode(value: V) = encoder(value)

        override fun decode(encoded: T) = decoder(encoded)

    }

}

inline fun <V, T> tagCodec(builder: ItemStackTagCodecBuilder<V, T>.() -> Unit) =
    ItemStackTagCodecBuilder<V, T>().apply(builder).create()

