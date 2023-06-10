package dev.zephyr.protocol.entity.data.display

import dev.zephyr.util.kotlin.KotlinOpens

@KotlinOpens
class Brightness @JvmOverloads constructor(block: Int = -1, sky: Int = -1) {

    var blockLight = block.coerceIn(-1..15)
        set(value) {
            field = value.coerceIn(-1..15)
        }

    var skyLight = sky.coerceIn(-1..15)
        set(value) {
            field = value.coerceIn(-1..15)
        }

    val value
        get() = if (blockLight == -1 || skyLight == -1) -1 else (blockLight shl 4) or (skyLight shl 20)

    companion object {

        val Default get() = Brightness()
        val Max get() = Brightness(15, 15)
        val Min get() = Brightness(0, 0)

    }

}