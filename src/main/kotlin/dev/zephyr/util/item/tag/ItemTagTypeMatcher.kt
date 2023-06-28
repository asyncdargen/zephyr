package dev.zephyr.util.item.tag

import dev.zephyr.util.item.TagType
import dev.zephyr.util.java.isStatic
import dev.zephyr.util.java.tryAccessAndGet
import org.apache.commons.lang.ClassUtils
import java.lang.reflect.Field

object ItemTagTypeMatcher {

    val TypeToTagTypeMap = TagType::class.java
        .declaredFields
        .filter(Field::isStatic)
        .mapNotNull { it.tryAccessAndGet<TagType<*>>() }
        .associateBy { it.primitiveType }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> match(type: Class<T>): TagType<T> {
        var rawType: Class<*> = type
        if (!rawType.isArray && rawType.isPrimitive) {
            rawType = ClassUtils.primitiveToWrapper(rawType)
        }

        return TypeToTagTypeMap[rawType] as TagType<T>
    }

}