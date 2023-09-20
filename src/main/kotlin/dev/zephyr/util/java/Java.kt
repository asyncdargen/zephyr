package dev.zephyr.util.java

import java.lang.invoke.MethodType
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
typealias JEnum<T> = java.lang.Enum<T>

fun uuidFromUnsignedString(uuid: String) = UUID(
    JLong.parseUnsignedLong(uuid.substring(0, 16).uppercase(), 16),
    JLong.parseUnsignedLong(uuid.substring(16, 32).uppercase(), 16)
)

fun Throwable?.throwIfNonNull() = this?.let { throw it }

fun openJavaModules(vararg modules: String) {
    val moduleClass = classOrNull<Any>("java.lang.Module") ?: return
    val moduleField = Class::class.java.getDeclaredField("module")

    val module = moduleField.getObjectWithUnsafe<Any>(Any::class.java)

    val addOpensMH = Lookup.findVirtual(
        moduleClass, "implAddOpens",
        MethodType.methodType(Void.TYPE, String::class.java)
    ).bindTo(module)

    addOpensMH.invoke("java.lang.invoke")
    addOpensMH.invoke("jdk.internal.misc")
    modules.forEach { addOpensMH.invoke(it) }
}