package dev.zephyr.protocol.entity.type.display

enum class DisplayBillBoard {


    /**
     * No rotation (default).
     */
    FIXED,

    /**
     * Can pivot around vertical axis.
     */
    VERTICAL,

    /**
     * Can pivot around horizontal axis.
     */
    HORIZONTAL,

    /**
     * Can pivot around center point.
     */
    CENTER;

    val id get() = ordinal.toByte()

}
