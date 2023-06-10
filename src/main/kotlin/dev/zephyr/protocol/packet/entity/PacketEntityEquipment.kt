package dev.zephyr.protocol.packet.entity

import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.Pair
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.inventory.ItemStack

@KotlinOpens
class PacketEntityEquipment : ProtocolPacket(PacketPlayOutType.ENTITY_EQUIPMENT) {

    var entityId by writer(0, integers)

    var items by writer(0, slotStackPairLists)
    var itemsMap by writer<Map<ItemSlot, ItemStack>> { items = it.map { (slot, item) -> Pair(slot, item) } }

}