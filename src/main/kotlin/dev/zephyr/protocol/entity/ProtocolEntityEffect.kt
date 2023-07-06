package dev.zephyr.protocol.entity

import dev.zephyr.protocol.packet.effect.PacketEntityEffect
import dev.zephyr.protocol.packet.effect.PacketEntityEffectRemove
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType

@KotlinOpens
class ProtocolEntityEffect(
    val type: PotionEffectType,
    var level: Int = 0,
    var duration: Int, var showParticles: Boolean = true,
    var ambient: Boolean = false, var showIcon: Boolean = true
) {

    fun add(entityId: Int, players: Collection<Player>) = PacketEntityEffect().also {
        it.entityId = entityId
        it.effectType = type
        it.level = level.toByte()
        it.duration = duration

        it.isShowIcon = showIcon
        it.isAmbient = ambient
        it.isShowParticles = showParticles
    }.send(players)

    fun add(entityId: Int, vararg players: Player) =
        add(entityId, players.toList())

    fun remove(entityId: Int, players: Collection<Player>) = PacketEntityEffectRemove().also {
        it.entityId = entityId
        it.effectType = type
    }.send(players)

    fun remove(entityId: Int, vararg players: Player) =
        remove(entityId, players.toList())


}