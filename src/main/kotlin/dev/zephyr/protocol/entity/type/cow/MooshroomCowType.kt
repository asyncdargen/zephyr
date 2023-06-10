package dev.zephyr.protocol.entity.type.cow

enum class MooshroomCowType {

    RED, BROWN;

    val type
        get() = name.lowercase()

}