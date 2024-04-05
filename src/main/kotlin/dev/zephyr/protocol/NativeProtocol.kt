package dev.zephyr.protocol

import com.comphenix.protocol.events.PacketContainer
import dev.zephyr.util.bukkit.craft
import dev.zephyr.util.bukkit.on
import dev.zephyr.util.collection.concurrentHashMapOf
import io.netty.buffer.ByteBuf
import io.netty.channel.Channel
import net.minecraft.network.NetworkManager
import net.minecraft.server.network.PlayerConnection
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.lang.reflect.Field
import java.util.*


private val MaterialId = Material::class.java.getDeclaredField("id").apply(Field::trySetAccessible)

fun ByteBuf.writeVarInt(value: Int) {
    var value = value

    while (true) {
        if (value and -0x80 == 0) {
            writeByte(value)
            return
        }
        writeByte((value and 0x7F) or 0x80)
        value = value ushr 7
    }
}

fun ByteBuf.writeVarLong(value: Long) {
    var value = value
    while (true) {
        if ((value and 0x7FL.inv()) == 0L) {
            writeByte(value.toInt())
            break
        }

        writeByte(((value and 0x7FL) or 0x80L).toInt())

        // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
        value = value ushr 7
    }
}

fun ByteBuf.writeEnum(enum: Enum<*>) = writeVarInt(enum.ordinal)

fun <T> ByteBuf.writeCollection(collection: Collection<T>, writer: ByteBuf.(T) -> Unit) {
    writeVarInt(collection.size)
    collection.forEach { writer(it) }
}

fun ByteBuf.writeBitSet(set: BitSet) {
    writeLongArray(set.toLongArray())
}

fun ByteBuf.writeLongArray(array: LongArray) {
    writeVarInt(array.size)
    array.forEach(this::writeLong)
}

fun ByteBuf.writeByteArray(array: ByteArray) {
    writeVarInt(array.size)
    writeBytes(array)
}

fun ByteBuf.writeByteBuf(buf: ByteBuf, index: Int, length: Int) {
    writeVarInt(length)
    writeBytes(buf, index, length)
}

fun ByteBuf.writeByteBuf(buf: ByteBuf, length: Int) = writeByteBuf(buf, 0, length)

fun ByteBuf.writeByteBuf(buf: ByteBuf) = writeByteBuf(buf, buf.writerIndex())

object NativeProtocol {

    private val NetworkManagerGetter: (PlayerConnection) -> NetworkManager = PlayerConnection::class
        .java
        .declaredFields
        .first { it.type == NetworkManager::class.java }
        .apply(Field::trySetAccessible)
        .let { { connection -> it[connection] as NetworkManager } }
    private val ChannelGetter: (NetworkManager) -> Channel = NetworkManager::class
        .java
        .declaredFields
        .first { it.type == Channel::class.java }
        .apply(Field::trySetAccessible)
        .let { { connection -> it[connection] as Channel } }

    val PlayerChannelsMap = concurrentHashMapOf<Player, Channel>()

    init {
        on<PlayerJoinEvent>(EventPriority.LOWEST) {
            val connection = player.craft().handle.b
            val networkManager = NetworkManagerGetter(connection)
            val channel = ChannelGetter(networkManager)

            PlayerChannelsMap[player] = channel
        }
        on<PlayerQuitEvent>(EventPriority.HIGHEST) { PlayerChannelsMap.remove(player) }
    }

    fun Player.sendForcePacket(any: Any) {
        when (any) {
            is PacketContainer-> PlayerChannelsMap[this@sendForcePacket]?.write(any.handle)
            else -> PlayerChannelsMap[this@sendForcePacket]?.write(any)
        }
    }

}