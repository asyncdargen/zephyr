package dev.zephyr.util.item

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import dev.zephyr.util.bukkit.craft
import dev.zephyr.util.java.classOrNull
import dev.zephyr.util.java.unreflect
import dev.zephyr.util.java.unreflectGetter
import dev.zephyr.util.kotlin.unit
import dev.zephyr.util.minecraft.MinecraftProfile
import dev.zephyr.util.minecraft.SkinTexture
import dev.zephyr.util.minecraft.skin
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

private val SkullMetaClass = classOrNull<SkullMeta>("org.bukkit.craftbukkit.v1_19_R3.inventory.CraftMetaSkull")!!

private val ProfileGetter = SkullMetaClass.getDeclaredField("profile").unreflectGetter()
private val ProfileSetter = SkullMetaClass.getDeclaredMethod("setProfile", GameProfile::class.java).unreflect()

fun skull(profile: GameProfile, block: ItemStack.() -> Unit = {}) = item(Material.PLAYER_HEAD) {
    skullGameProfile = profile
    block()
}

fun skull(profile: MinecraftProfile, block: ItemStack.() -> Unit = {}) = item(Material.PLAYER_HEAD) {
    skullProfile = profile
    block()
}

fun skull(skin: SkinTexture, block: ItemStack.() -> Unit = {}) = item(Material.PLAYER_HEAD) {
    skullTexture = skin
    block()
}

fun skull(value: String?, signature: String?, block: ItemStack.() -> Unit = {}) =
    skull(skin(value, signature), block)

fun skull(player: Player, block: ItemStack.() -> Unit = {}) = item(Material.PLAYER_HEAD) {
    skullOwner = player
    block()
}

fun skull(name: String, block: ItemStack.() -> Unit = {}) = item(Material.PLAYER_HEAD) {
    skullOwnerName = name
    block()
}

fun skull(uuid: UUID, block: ItemStack.() -> Unit = {}) = item(Material.PLAYER_HEAD) {
    skullOwnerUUID = uuid
    block()
}

var ItemStack.skullGameProfile
    set(value) = metaAs<SkullMeta> { ProfileSetter.invoke(this, value) }.unit()
    get() = ProfileGetter.invoke(meta) as GameProfile?

var ItemStack.skullTexture
    set(value) {
        skullGameProfile = GameProfile(UUID.randomUUID(), "dargen").apply {
            properties.put("textures", Property("textures", value?.value, value?.signature))
        }
    }
    get() = skullGameProfile?.properties?.get("textures")?.firstOrNull()?.let { SkinTexture(it.value, it.signature) }

var ItemStack.skullProfile
    set(value) {
        skullTexture = value?.skin
    }
    get() = skullGameProfile?.run { MinecraftProfile(id, name, skullTexture) }

var ItemStack.skullOwner
    set(value) {
        skullGameProfile = value?.craft()?.profile
    }
    get() = skullGameProfile?.name?.let(Bukkit::getPlayer)

var ItemStack.skullOwnerName
    set(value) {
        skullProfile = value?.let(MinecraftProfile::get)
    }
    get() = skullGameProfile?.name

var ItemStack.skullOwnerUUID
    set(value) {
        skullProfile = value?.let(MinecraftProfile::get)
    }
    get() = skullGameProfile?.id