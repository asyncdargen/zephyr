package dev.zephyr.protocol.entity.type.display

enum class TextDisplayAlignment {

    CENTER,
    LEFT,
    RIGHT;

    val id get() = ordinal * 8

}