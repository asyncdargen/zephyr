package dev.zephyr.util.minecraft

import com.google.common.cache.CacheBuilder
import com.google.gson.JsonObject
import dev.zephyr.Zephyr
import dev.zephyr.extensions.build
import dev.zephyr.util.java.uuidFromUnsignedString
import org.apache.commons.io.IOUtils.toString
import java.net.URL
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

data class Skin(val uuid: UUID, val name: String, val texture: SkinTexture) {

    companion object {

        val Null = Skin(UUID.randomUUID(), "null", SkinTexture(null, null))

        private val UUID2SkinCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.HOURS)
            .build<UUID, CompletableFuture<Skin>> {
                CompletableFuture.supplyAsync {
                    runCatching {
                        toString(
                            URL("https://sessionserver.mojang.com/session/minecraft/profile/$it?unsigned=false"),
                            Charsets.UTF_8
                        ).run { Zephyr.Gson.fromJson(this, JsonObject::class.java) }.run {
                            Skin(
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
                            .run { Zephyr.Gson.fromJson(this, JsonObject::class.java)["id"].asString }
                            .run { uuidFromUnsignedString(this) }
                    }.getOrNull()
                }
            }

        fun getLazy(uuid: UUID): CompletableFuture<Skin> = UUID2SkinCache.get(uuid)

        fun get(uuid: UUID): Skin = getLazy(uuid).get(8, TimeUnit.SECONDS)

        fun getLazy(name: String): CompletableFuture<Skin> = Name2UUIDCache.get(name.lowercase()).thenComposeAsync {
            it?.let(this::getLazy) ?: CompletableFuture.completedFuture(Null)
        }

        fun get(name: String): Skin = getLazy(name).get(8, TimeUnit.SECONDS)

    }

}

data class SkinTexture(val value: String?, val signature: String?)