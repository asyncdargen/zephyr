package dev.zephyr.protocol.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.StructureModifier
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataValue
import dev.zephyr.protocol.Protocol
import dev.zephyr.protocol.entity.metadata.Metadata
import dev.zephyr.util.bukkit.toWrappedChatComponent
import dev.zephyr.util.kotlin.KotlinOpens
import it.unimi.dsi.fastutil.ints.IntList
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.entity.Player
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@KotlinOpens
class ProtocolPacket(type: PacketType, block: ((ProtocolPacket) -> Unit)? = null) : PacketContainer(type) {

    init {
        modifier.writeDefaults()

        block?.invoke(this)
    }

    fun receive(players: Collection<Player>) =
        players.forEach { Protocol.ProtocolManager.receiveClientPacket(it, this) }

    fun receive(vararg players: Player) =
        players.forEach { Protocol.ProtocolManager.receiveClientPacket(it, this) }

    fun send(players: Collection<Player>) =
        players.forEach { Protocol.ProtocolManager.sendServerPacket(it, this) }

    fun send(vararg players: Player) =
        players.forEach { Protocol.ProtocolManager.sendServerPacket(it, this) }

    fun broadcast() = Protocol.ProtocolManager.broadcastServerPacket(this)

    class ProtocolProperty<T>(
        val writer: ProtocolPacket.(T) -> Unit
    ) : ReadWriteProperty<ProtocolPacket, T> {

        override fun setValue(thisRef: ProtocolPacket, property: KProperty<*>, value: T) {
            writer(thisRef, value)
        }

        override fun getValue(thisRef: ProtocolPacket, property: KProperty<*>): T =
            throw UnsupportedOperationException()

    }

    companion object {

        val VelocityMapper: (Double) -> Int = { it.times(8000).toInt() }
        val MoveMapper: (Double) -> Short = { it.times(32).times(128).toInt().toShort() }
        val AngleMapper: (Float) -> Byte = { it.times(256F).div(360F).toInt().toByte() }
        val ChatComponentMapper: (String) -> WrappedChatComponent = WrappedChatComponent::fromLegacyText
        val KyoriComponentMapper: (Component) -> WrappedChatComponent = { it.toWrappedChatComponent() }
        val MetadataMapper: (Metadata) -> List<WrappedDataValue> = { it.items.toList() }
        val IntListMapper: (Collection<Int>) -> IntList = { IntList.of(*it.toIntArray()) }

        fun <T> writer(writer: ProtocolPacket.(T) -> Unit) =
            ProtocolProperty(writer)

        fun <T, V> writer(mapper: (T) -> V, writer: ProtocolPacket.(V) -> Unit) =
            ProtocolProperty<T> { writer(mapper(it)) }

        fun <T> writer(index: Int, modifier: StructureModifier<T>) =
            writer<T> { modifier.write(index, it) }

        fun <T, V> writer(index: Int, modifier: StructureModifier<V>, mapper: (T) -> V) =
            writer<T> { modifier.write(index, mapper(it)) }

    }

}

operator fun <P : ProtocolPacket> P.plus(block: (P) -> Unit) = apply(block)