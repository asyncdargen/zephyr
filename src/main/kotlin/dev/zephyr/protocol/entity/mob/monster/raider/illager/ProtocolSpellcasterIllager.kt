package dev.zephyr.protocol.entity.mob.monster.raider.illager

import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.raid.SpellcasterSpell
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolSpellcasterIllager(type: EntityType, location: Location) : ProtocolAbstractIllager(type, location) {

    var spell by metadata.item(17, MetadataType.Byte, SpellcasterSpell.NONE) { it.ordinal.toByte() }

}