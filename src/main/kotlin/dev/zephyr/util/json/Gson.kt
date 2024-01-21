package dev.zephyr.util.json

import com.google.gson.*
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

typealias Json = SerializedName

val GsonBuilder = GsonBuilder()
var Gson = GsonBuilder.create()

fun gson(bake: Boolean = true, block: GsonBuilder.() -> Unit): Gson {
    GsonBuilder.block()
    if (bake) Gson = GsonBuilder.create()
    return Gson
}

inline fun <reified T : Any> GsonBuilder.deserializer(
    type: Type = type<T>(),
    noinline deserializer: (element: JsonElement, ctx: JsonDeserializationContext) -> T
) = registerTypeAdapter(type, DelegateJsonDeserializer(deserializer))

inline fun <reified T : Any> GsonBuilder.serializer(
    type: Type = type<T>(),
    noinline serializer: (any: T, ctx: JsonSerializationContext) -> JsonElement
) = registerTypeAdapter(type, DelegateJsonSerializer(serializer))

inline fun <reified T : Any> GsonBuilder.adapter(
    noinline serializer: (obj: T, ctx: JsonSerializationContext) -> JsonElement,
    noinline deserializer: (element: JsonElement, ctx: JsonDeserializationContext) -> T,
    type: Type = type<T>()
) = deserializer(type, deserializer).serializer(type, serializer)

inline fun <reified T : Any> type() = object : TypeToken<T>() {}.type

inline fun <reified T : Any> fromJson(json: String, type: Type = type<T>()) = Gson.fromJson<T>(json, type)

inline fun <reified T : Any> fromJson(json: JsonElement, type: Type = type<T>()) = Gson.fromJson<T>(json, type)

fun toJson(any: Any) = Gson.toJson(any)

fun toJsonTree(any: Any) = Gson.toJsonTree(any)