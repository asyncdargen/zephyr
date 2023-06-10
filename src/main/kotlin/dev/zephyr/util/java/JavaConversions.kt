package dev.zephyr.util.java

import java.util.*

typealias JBoolean = java.lang.Boolean
typealias JByte = java.lang.Byte
typealias JChar = Character
typealias JShort = java.lang.Short
typealias JInt = java.lang.Integer
typealias JLong = java.lang.Long
typealias JFloat = java.lang.Float
typealias JDouble = java.lang.Double
typealias JString = java.lang.String

typealias JLogger = java.util.logging.Logger

fun uuidFromUnsignedString(uuid: String) = UUID(
    JLong.parseUnsignedLong(uuid.substring(0, 16).uppercase(), 16),
    JLong.parseUnsignedLong(uuid.substring(16, 32).uppercase(), 16)
)