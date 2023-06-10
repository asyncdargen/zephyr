package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.EntityAnimation
import dev.zephyr.protocol.packet.entity.PacketEntityAnimation
import dev.zephyr.protocol.packet.entity.PacketEntityEquipment
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

typealias Equipment = MutableMap<EnumWrappers.ItemSlot, ItemStack>

@KotlinOpens
class ProtocolLivingEntity(type: EntityType, location: Location) : ProtocolEntity(type, location) {

    var isActiveHand by metadata.bitMask(8, 0x01)
    var activeHand by metadata.bitMask(8, EnumWrappers.Hand.MAIN_HAND) { it.ordinal * 0x02 }
    var isInRiptideAttack by metadata.bitMask(8, 0x04)

    var health by metadata.item(9, MetadataType.Float, 1f)
    var potionEffectColor by metadata.item(10, MetadataType.Int, 0) //todo: check types
    var isPotionEffectAmbient by metadata.item(11, MetadataType.Boolean, false)
    var arrowsCount by metadata.item(12, MetadataType.Int, 0)
    var beeStingersCount by metadata.item(13, MetadataType.Int, 0)

    val equipment: Equipment = EnumMap<EnumWrappers.ItemSlot, ItemStack>(EnumWrappers.ItemSlot::class.java)
        .apply { put(EnumWrappers.ItemSlot.MAINHAND, ItemStack(Material.AIR)) }

    override fun sendSpawnPackets(players: Collection<Player>) {
        super.sendSpawnPackets(players)
        sendEquipment(players = players)
    }

    fun sendEquipment(items: Equipment = equipment, players: Collection<Player>) = PacketEntityEquipment().also {
        it.entityId = id
        it.itemsMap = items
    }.sendOrSendAll(players)

    fun sendEquipment(items: Equipment = equipment, vararg players: Player) = sendEquipment(items, players.toList())

    fun equip(slot: EnumWrappers.ItemSlot, itemStack: ItemStack, players: Collection<Player>) {
        val equipment = if (players.isEmpty()) this.equipment else hashMapOf()
        equipment[slot] = itemStack

        sendEquipment(equipment, players)
    }

    fun equip(slot: EnumWrappers.ItemSlot, itemStack: ItemStack, vararg players: Player) =
        equip(slot, itemStack, players.toList())


    fun animate(animation: EntityAnimation, players: Collection<Player>) = PacketEntityAnimation().also {
        it.entityId = id
        it.animationId = animation.ordinal
    }.sendOrSendAll(players)

    fun animate(animation: EntityAnimation, vararg players: Player) = animate(animation, players.toList())

}