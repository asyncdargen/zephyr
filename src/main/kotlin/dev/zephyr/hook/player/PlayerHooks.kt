@file:Suppress("DEPRECATION")

package dev.zephyr.hook.player

import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedGameProfile
import dev.zephyr.hook.animation.PlayerItemAnimationHolder
import dev.zephyr.hook.vehicle.ProtocolPlayerVehicleSupport
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.packet.player.PacketGameEvent
import dev.zephyr.protocol.packet.player.PacketPlayerInfoUpdate
import dev.zephyr.util.bukkit.craft
import dev.zephyr.util.bukkit.everyAsync
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.bukkit.players
import dev.zephyr.util.collection.takeIfNotEmpty
import dev.zephyr.util.java.isStatic
import dev.zephyr.util.java.tryAccessAndGet
import dev.zephyr.util.kotlin.cast
import dev.zephyr.util.kotlin.safeCast
import net.minecraft.core.IRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.syncher.DataWatcherObject
import net.minecraft.resources.MinecraftKey
import net.minecraft.world.entity.player.EntityHuman
import net.minecraft.world.flag.FeatureElement
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.flag.FeatureFlags
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

object PlayerHooks {

    val AbsorptionDataWatcherObject = EntityHuman::class.java
        .declaredFields
        .first { it.type == DataWatcherObject::class.java }
        .tryAccessAndGet<DataWatcherObject<Float>>()

    init {
        ProtocolPlayerVehicleSupport
        PlayerItemAnimationHolder
    }

    fun hideHealth() {
        on<PlayerJoinEvent>(EventPriority.LOWEST) { player.hideHealth() }
        everyAsync(20, 20) { players().forEach(Player::hideHealth) }
    }

    fun enableNewFeatures() = Protocol.onSend(PacketPlayOutType.UPDATE_ENABLED_FEATURES) {
        packet.modifier.write(0, listOf("update_1_20", "vanilla", "bundle").map(MinecraftKey::a).toSet())

        fun Class<*>.declaredFields(): List<Field> {
            return if (this == Any::class.java) emptyList()
            else declaredFields.toList() + superclass.declaredFields
        }

        val vanillaFeatures = FeatureFlags.f
        BuiltInRegistries::class.java
            .declaredFields
            .filter(Field::isStatic)
            .filter { Registry::class.java.isAssignableFrom(it.type) }
            .forEach { field ->
                runCatching {
                    val flagFields = field.genericType
                        .safeCast<ParameterizedType>()
                        ?.actualTypeArguments
                        ?.getOrNull(0)
                        ?.safeCast<Class<*>>()
                        ?.takeIf { FeatureElement::class.java.isAssignableFrom(it) }
                        ?.declaredFields()
                        ?.filter { it.type == FeatureFlagSet::class.java }
                        ?.onEach { it.trySetAccessible() }
                        ?.takeIfNotEmpty()
                        ?: return@forEach

                    val registry = field[null].cast<IRegistry<*>>()
                    registry.s().forEach { flagFields.forEach { field -> field[it] = vanillaFeatures } }
                }
            }
    }

}

fun Player.applyFakeGameMode(mode: GameMode) {
    PacketPlayerInfoUpdate().also {
        it.actions = setOf(PlayerInfoAction.UPDATE_GAME_MODE)
        it.playerInfos = listOf(
            PlayerInfoData(
                uniqueId, -1, true,
                NativeGameMode.fromBukkit(mode),
                WrappedGameProfile.fromPlayer(this),
                WrappedChatComponent.fromLegacyText("")
            )
        )
    }.send(this)
    PacketGameEvent().also {
        it.type = 3
        it.value = mode.value.toFloat()
    }.send(this)
}

fun Player.hideHealth() = craft().handle.aj().b(PlayerHooks.AbsorptionDataWatcherObject, -1_000_000_00f)

