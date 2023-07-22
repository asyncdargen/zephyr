package dev.zephyr.hook.player

import com.google.common.cache.CacheBuilder
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.ProtocolVehicle
import dev.zephyr.protocol.entity.mob.animal.ProtocolTurtle
import dev.zephyr.protocol.entity.type.display.DisplayBillBoard
import dev.zephyr.protocol.entity.world.display.*
import dev.zephyr.protocol.packet.entity.PacketEntityMount
import dev.zephyr.util.bukkit.*
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.java.tryAccessAndGet
import dev.zephyr.util.kotlin.KotlinOpens
import net.minecraft.network.syncher.DataWatcherObject
import net.minecraft.resources.MinecraftKey
import net.minecraft.world.entity.player.EntityHuman
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.concurrent.TimeUnit

object PlayerHooks {


    val PlayerVehicles = concurrentHashMapOf<Player, PlayerVehicle>()
    val ItemAnimations = CacheBuilder.newBuilder()
        .expireAfterWrite(2050, TimeUnit.MILLISECONDS)
        .build<Player, PlayerItemAnimation>()
    val AbsorptionDataWatcherObject = EntityHuman::class.java
        .declaredFields
        .first { it.type == DataWatcherObject::class.java }
        .tryAccessAndGet<DataWatcherObject<Float>>()

    init {
        everyAsync(25, 25) {
            PlayerVehicles.values.forEach(ProtocolVehicle::syncMounts)
        }
        on<PlayerTeleportEvent>(EventPriority.LOWEST) {
            PlayerVehicles[player]?.apply {
                syncMounts()
            }
        }
        on<PlayerJoinEvent>(EventPriority.LOWEST) { PlayerVehicles[player] = PlayerVehicle(player) }
        on<PlayerQuitEvent>(EventPriority.LOWEST) { PlayerVehicles.remove(player)?.clear() }
    }

    fun hideHealth() {
        on<PlayerJoinEvent>(EventPriority.LOWEST) { player.hideHealth() }
        everyAsync(20, 20) { players().forEach(Player::hideHealth) }
    }

    fun enableNewFeatures() = Protocol.onSend(PacketPlayOutType.UPDATE_ENABLED_FEATURES) {
        packet.modifier.write(0, listOf("update_1_20", "vanilla", "bundle").map(MinecraftKey::a).toSet())
    }

    fun showItem(player: Player, item: ItemStack) =
        ItemAnimations.asMap().put(player, PlayerItemAnimation(player, item))?.remove()

}

fun Player.hideHealth() = craft().handle.aj().b(PlayerHooks.AbsorptionDataWatcherObject, -1_000_000_00f)

fun Player.showItem(item: ItemStack) = PlayerHooks.showItem(this, item)

fun Player.mount(vararg entities: ProtocolEntity) = PlayerHooks.PlayerVehicles[this]?.mount(*entities)

fun Player.unmount(vararg entities: ProtocolEntity) = PlayerHooks.PlayerVehicles[this]?.unmount(*entities)

@KotlinOpens
class PlayerItemAnimation(val player: Player, item: ItemStack) : ProtocolItemDisplay(player.location, item) {

    val displaySlot = ProtocolTurtle(location).apply {
        isBaby = true
        isInvisible = true
        player.mount(this)
    }

    init {
        fullBright()
        removeShadow()
        maxViewRange()

        scale = Vector3f(0f, 0f, 0f)
        translation = Vector3f(0f, .025f, -.1f)
        billboard = DisplayBillBoard.CENTER

        displaySlot.mount(this)
        displaySlot.spawn(player)

        spawn(player)

        animate()
    }

    fun animate() {
        interpolate(1, 7) {
            scaleUp()
            rollAroundFront()
        } next {
            interpolate(0, 13) {
                waveLeft()
            }
        } next {
            interpolate(0, 13) {
                waveRight()
            }
        } next {
            interpolate(0, 7) {
                scaleDown()
                rollAroundBack()
            }
        } after {
            stop()
        }
    }

    fun stop() {
        interpolation.cancel()

        remove()
        displaySlot.remove()
    }

    private fun waveLeft() {
        translation = Vector3f(0f, .025f, -.1f)

        leftRotation = Quaternionf(0.07f, -1f, 0f, 0f)
    }

    private fun waveRight() {
        translation = Vector3f(0.04f, .025f, -.1f)

        leftRotation = Quaternionf(-0.05f, -1f, 0f, 0f)
    }

    private fun scaleUp() {
        scale = Vector3f(.08f, .08f, .08f)
    }

    private fun scaleDown() {
        scale = Vector3f(0f, 0f, 0f)
    }

    private fun rollAroundFront() {
        translation = Vector3f(0.025f, .025f, -.1f)

        rightRotation = rightRotation.rotationY(-3f)
        leftRotation = leftRotation.rotationY(-3f)

    }

    private fun rollAroundBack() {
        translation = Vector3f(0f, .025f, -.1f)

        rightRotation = rightRotation.rotationY(6f)
        leftRotation = leftRotation.rotationY(6f)
    }

}

@KotlinOpens
class PlayerVehicle(val player: Player) : ProtocolVehicle {

    override val mounts = mutableSetOf<ProtocolEntity>()

    override fun sendMounts(players: Collection<Player>) = PacketEntityMount().also {
        it.entityId = player.entityId
        it.entities = (mounts.map(ProtocolEntity::entityId) + player.passengers.map(Entity::getEntityId)).toIntArray()
    }.send(players)

    override fun syncMounts() {
        mounts.forEach { it.location = player.location.clearAngles() }
    }

    override fun mount(entities: Collection<ProtocolEntity>) {
        entities.forEach { it.vehicle = this }
        mounts.addAll(entities)
        syncMounts()
        entities.firstOrNull()?.viewers?.let { sendMounts(it) }
    }

    override fun unmount(entities: Collection<ProtocolEntity>) {
        mounts.removeAll(entities)
        entities.firstOrNull()?.viewers?.let { sendMounts(it) }
    }

    fun clear() = unmount(*mounts.toTypedArray())

}