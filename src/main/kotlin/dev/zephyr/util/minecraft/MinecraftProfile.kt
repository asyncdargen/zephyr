package dev.zephyr.util.minecraft

import com.google.common.cache.CacheBuilder
import com.google.gson.JsonObject
import dev.zephyr.util.java.uuidFromUnsignedString
import dev.zephyr.util.json.fromJson
import dev.zephyr.util.kotlin.build
import org.apache.commons.io.IOUtils.toString
import java.net.URL
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

data class MinecraftProfile(val uuid: UUID, val name: String, val skin: SkinTexture?) {

    companion object {

        val Null = MinecraftProfile(UUID.randomUUID(), "null", null)

        private val UUID2SkinCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.HOURS)
            .build<UUID, CompletableFuture<MinecraftProfile>> {
                CompletableFuture.supplyAsync {
                    runCatching {
                        toString(
                            URL("https://sessionserver.mojang.com/session/minecraft/profile/$it?unsigned=false"),
                            Charsets.UTF_8
                        ).run { fromJson<JsonObject>(this) }.run {
                            MinecraftProfile(
                                it, get("name").asString,
                                get("properties").asJsonArray.get(0).asJsonObject.run {
                                    SkinTexture(get("value")?.asString, get("signature")?.asString)
                                }
                            )
                        }
                    }.getOrNull() ?: Null
                }
            }
        private val Name2UUIDCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.HOURS)
            .build<String, CompletableFuture<UUID?>> {
                CompletableFuture.supplyAsync {
                    runCatching {
                        toString(URL("https://api.mojang.com/users/profiles/minecraft/$it"), Charsets.UTF_8)
                            .run { fromJson<JsonObject>(this)["id"].asString }
                            .run { uuidFromUnsignedString(this) }
                    }.getOrNull()
                }
            }

        fun getLazy(uuid: UUID): CompletableFuture<MinecraftProfile> = UUID2SkinCache.get(uuid)

        fun get(uuid: UUID): MinecraftProfile = getLazy(uuid).get(8, TimeUnit.SECONDS)

        fun getLazy(name: String): CompletableFuture<MinecraftProfile> = Name2UUIDCache.get(name.lowercase()).thenComposeAsync {
            it?.let(this::getLazy) ?: CompletableFuture.completedFuture(Null)
        }

        fun get(name: String): MinecraftProfile = getLazy(name).get(8, TimeUnit.SECONDS)

    }

}

fun skinLazy(uuid: UUID) = MinecraftProfile.getLazy(uuid).thenApply(MinecraftProfile::skin)

fun skin(uuid: UUID) = skinLazy(uuid).get(8, TimeUnit.SECONDS)

fun skinLazy(name: String) = MinecraftProfile.getLazy(name).thenApply(MinecraftProfile::skin)

fun skin(name: String) = skinLazy(name).get(8, TimeUnit.SECONDS)

fun skin(value: String?, signature: String?) = SkinTexture(value, signature)

data class SkinTexture(val value: String?, val signature: String?)