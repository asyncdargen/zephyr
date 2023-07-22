package dev.zephyr.protocol.entity.world.living

import com.comphenix.protocol.wrappers.EnumWrappers.*
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedGameProfile
import com.comphenix.protocol.wrappers.WrappedSignedProperty
import com.comphenix.protocol.wrappers.nbt.NbtFactory
import com.mojang.authlib.GameProfile
import dev.zephyr.protocol.entity.ProtocolLivingEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.world.display.ProtocolTextDisplay
import dev.zephyr.protocol.packet.player.PacketPlayerInfoRemove
import dev.zephyr.protocol.packet.player.PacketPlayerInfoUpdate
import dev.zephyr.protocol.packet.player.PacketPlayerSpawn
import dev.zephyr.protocol.scoreboard.ProtocolScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.util.bukkit.at
import dev.zephyr.util.bukkit.diff
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import dev.zephyr.util.minecraft.MinecraftProfile
import dev.zephyr.util.minecraft.SkinTexture
import dev.zephyr.util.minecraft.skin
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*

@KotlinOpens
class ProtocolPlayer(val skin: SkinTexture?, location: Location) : ProtocolLivingEntity(EntityType.PLAYER, location) {
    constructor(name: String, location: Location) : this(skin(name), location)
    constructor(uuid: UUID, location: Location) : this(skin(uuid), location)
    constructor(profile: MinecraftProfile, location: Location) : this(profile.skin, location)
    constructor(profile: GameProfile, location: Location) : this(profile.id, location)
    constructor(location: Location) : this(null, location)

    var additionalHearts by metadata.item(15, MetadataType.Float, 0f)
    var score by metadata.item(16, MetadataType.Int, 0)
    var displaySkinParts by metadata.bitMask(17, 0x7F).apply { update(true) }
    var mainHand by metadata.item(18, MetadataType.Byte, Hand.MAIN_HAND) { it.ordinal.toByte() }

    var leftShoulderEntityData by metadata.item(19, MetadataType.NBT, NbtFactory.ofCompound("dummy"))
    var rightShoulderEntityData by metadata.item(20, MetadataType.NBT, NbtFactory.ofCompound("dummy"))

    val profile = WrappedGameProfile(uuid, RandomStringUtils.randomAlphanumeric(16)).apply {
        skin?.let { properties.put("textures", WrappedSignedProperty("textures", it.value, it.signature)) }
    }
    override val team = ProtocolScoreboardTeam(uuid, profile.name).apply {
        collision = ScoreboardTeamCollision.NEVER
        visibility = ScoreboardTeamTagVisibility.NEVER
    }

    override var location by observable(location) { _, location -> title.teleport(location.diff(y = 2).at(pitch = 0)) }
    override var accessor
        get() = super.accessor
        set(value) {
            super.accessor = value
            title.accessor = value
        }

    val title = ProtocolTextDisplay(location.diff(y = 2).at(pitch = 0))

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendPlayerInfo(PlayerInfoAction.ADD_PLAYER, players)
        super.sendSpawnPackets(players)
    }

    override fun sendSpawn(players: Collection<Player>) = PacketPlayerSpawn().also {
        it.entityId = entityId
        it.entityUUID = uuid

        it.location = location
    }.sendOrSendAll(players)

    override fun sendDestroyPackets(players: Collection<Player>) {
        super.sendDestroyPackets(players)
        sendPlayerInfoRemove(players)
    }

    fun sendPlayerInfo(action: PlayerInfoAction, players: Collection<Player>) = PacketPlayerInfoUpdate().also {
        it.actions = EnumSet.of(action)
        it.playerInfos = listOf(
            PlayerInfoData(
                uuid, -1, false,
                NativeGameMode.ADVENTURE, profile,
                WrappedChatComponent.fromText(profile.name)
            )
        )
    }.sendOrSendAll(players)

    fun sendPlayerInfo(action: PlayerInfoAction, vararg players: Player) =
        sendPlayerInfo(action, players.toList())

    fun sendPlayerInfoRemove(players: Collection<Player>) = PacketPlayerInfoRemove().also {
        it.playersUUIDs = listOf(uuid)
    }.sendOrSendAll(players)

    fun sendPlayerInfoRemove(vararg players: Player) =
        sendPlayerInfoRemove(players.toList())

    override fun remove() {
        super.remove()
        title.remove()
    }

    override fun registerEntity() {
        title.registerEntity()
        super.registerEntity()
    }

}