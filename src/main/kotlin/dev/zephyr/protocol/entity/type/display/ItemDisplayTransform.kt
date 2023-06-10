package dev.zephyr.protocol.entity.type.display

enum class ItemDisplayTransform {


    NONE,
    THIRDPERSON_LEFTHAND,
    THIRDPERSON_RIGHTHAND,
    FIRSTPERSON_LEFTHAND,
    FIRSTPERSON_RIGHTHAND,
    HEAD,
    GUI,
    GROUND,
    FIXED;

    val id get() = ordinal.toByte()

}
