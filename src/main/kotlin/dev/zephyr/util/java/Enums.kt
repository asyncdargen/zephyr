package dev.zephyr.util.java

import ru.dargen.crowbar.Accessors.unsafe
import java.lang.reflect.Field

val EnumNameAccessor = unsafe().openField<String>(Enum::class.java, "name")
val EnumOrdinalAccessor = unsafe().openField<Int>(Enum::class.java, "ordinal")

inline fun <reified E : Enum<E>> append(name: String, vararg values: Any?): E {
    val enumClass = E::class.java
    val valuesAccessor = unsafe().openField<Array<E?>>(enumClass, "\$VALUES")
    val enumValues = valuesAccessor.staticValue.run { copyOf(size + 1) }
    val ordinal = enumValues.size - 1

    val value = ru.dargen.crowbar.util.Unsafe.allocateInstance(E::class.java)
    EnumNameAccessor.setValue(value, name)
    EnumOrdinalAccessor.setValue(value, ordinal)

    enumClass.declaredFields
        .filterNot(Field::isStatic)
        .forEachIndexed { index, field -> unsafe().openField<Any?>(field).setValue(value, values[index]) }

    enumValues[ordinal] = value
    valuesAccessor.staticValue = enumValues

    return value
}