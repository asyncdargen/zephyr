package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.WrappedChatComponent
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.asChunkPointer
import dev.zephyr.protocol.entity.metadata.MetadataItem
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.metadata.ObservableMetadata
import dev.zephyr.protocol.entity.type.EntityInteract
import dev.zephyr.protocol.packet.entity.*
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardTeam
import dev.zephyr.protocol.scoreboard.ProtocolScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamAction
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.util.bukkit.loadedByPlayers
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.collection.ifNotEmpty
import dev.zephyr.util.collection.observe
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@KotlinOpens
class ProtocolEntity(
    val id: Int, val uuid: UUID,
    val type: EntityType, val spawnData: Int,
    location: Location
) : ProtocolObject() {
    constructor(uuid: UUID, type: EntityType, spawnData: Int, location: Location) :
            this(nextEntityId(), uuid, type, spawnData, location)

    constructor(uuid: UUID, type: EntityType, location: Location) :
            this(uuid, type, 0, location)

    constructor(type: EntityType, spawnData: Int, location: Location) :
            this(UUID.randomUUID(), type, spawnData, location)

    constructor(type: EntityType, location: Location) :
            this(UUID.randomUUID(), type, 0, location)

    var clickHandler: (Player, EntityInteract) -> Unit = { _, _ -> }
    var accessor: (Player) -> Boolean = { true }

    val effects = concurrentSetOf<ProtocolEntityEffect>().observe({ it.add(id, viewers) }, { it.remove(id, viewers) })
    val mounts = mutableSetOf<ProtocolEntity>()
    var vehicle: ProtocolEntity? = null

    val metadata = ObservableMetadata.create(this)
    val team = ProtocolScoreboardTeam(uuid.toString().replace("-", "").substring(16), uuid.toString())

    var isBurning by metadata.bitMask(0, 0x01)
    var isSneaking by metadata.bitMask(0, 0x02)
    var isSprinting by metadata.bitMask(0, 0x08)
    var isSwimming by metadata.bitMask(0, 0x10)
    var isInvisible by metadata.bitMask(0, 0x20)
    var isGlowing by metadata.bitMask(0, 0x40)
    var isElytraFlying by metadata.bitMask(0, 0x80)

    var glowColor by observable(ChatColor.RESET) { _, value -> team.color = value }
    var ticksAir by metadata.item(1, MetadataType.Int, 0)
    var customName by metadata.item<String?, Optional<WrappedChatComponent>>(
        2, MetadataType.ChatComponentOptional, null
    ) { Optional.ofNullable(it?.let(WrappedChatComponent::fromLegacyText)) } on {
        isCustomNameVisible = it.value != null
    }
    var isCustomNameVisible by metadata.item(3, MetadataType.Boolean, false)
    var isSilent by metadata.item(4, MetadataType.Boolean, false)
    var isNoGravity by metadata.item(5, MetadataType.Boolean, false)
    var pose by metadata.item(6, MetadataType.Pose, EnumWrappers.EntityPose.STANDING)
    var ticksFrozen by metadata.item(7, MetadataType.Int, 0)
    var isOnGround by observable(true) { _, _ -> teleport() }

    var location by observable(location) { _, _ -> spawnLocal() }
    val chunkPointer get() = location.asChunkPointer()
    val chunk by location::chunk
    val world by location::world

    init {
        if (EntityProtocol.AutoRegister) {
            register()
        }
    }

    override fun spawn(players: Collection<Player>) {
        super.spawn(players)
        team.spawn(players)
    }

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendSpawn(players)
        sendLook(players = players)
        sendMetadata(players = players)

        sendEffects(players = players)
        sendMount(players = players)
        vehicle?.sendMount(players = players)
    }

    fun sendSpawn(players: Collection<Player>) = PacketEntitySpawn().also {
        it.entityId = id
        it.entityUUID = uuid

        it.entityType = type
        it.entityData = spawnData

        it.location = location
        it.velocity = Vector(.0, .0, .0)
    }.sendOrSendAll(players)

    fun sendSpawn(vararg players: Player) =
        sendSpawn(players.toList())

    override fun destroy(players: Collection<Player>) {
        super.destroy(players)
        team.destroy(players)
    }

    override fun sendDestroyPackets(players: Collection<Player>) {
        team.destroy(players)

        sendDestroy(players)
    }

    fun sendDestroy(players: Collection<Player>) = PacketEntityDestroy().also {
        it.entitiesIds = listOf(id)
    }.sendOrSendAll(players)

    fun sendDestroy(vararg players: Player) =
        sendDestroy(players.toList())

    fun setPrivateCustomName(customName: String, players: Collection<Player>) =
        modifySpecial(players) { this.customName = customName }

    fun setPrivateCustomName(customName: String, vararg players: Player) =
        setPrivateCustomName(customName, players.toList())

    fun sendStatus(status: Int, players: Collection<Player>) = PacketEntityStatus().also {
        it.entityId = id
        it.status = status.toByte()
    }.sendOrSendAll(players)


    /**
    https://wiki.vg/Entity_statuses
     **/
    fun sendStatus(status: Int, vararg players: Player) =
        sendStatus(status, players.toList())

    fun sendEffects(effects: Collection<ProtocolEntityEffect> = this.effects, players: Collection<Player>) =
        effects.forEach { it.add(id, players) }

    fun sendEffects(effects: Collection<ProtocolEntityEffect> = this.effects, vararg players: Player) =
        sendEffects(effects, players.toList())

    fun sendRemoveEffects(effects: Collection<ProtocolEntityEffect> = this.effects, players: Collection<Player>) =
        effects.forEach { it.remove(id, players) }

    fun sendRemoveEffects(effects: Collection<ProtocolEntityEffect> = this.effects, vararg players: Player) =
        sendRemoveEffects(effects, players.toList())

    fun sendVelocity(velocity: Vector, players: Collection<Player>) = PacketEntityVelocity().also {
        it.entityId = id
        it.velocity = velocity
    }.sendOrSendAll(players)

    fun sendVelocity(velocity: Vector, vararg players: Player) =
        sendVelocity(velocity, players.toList())


    fun sendTeleport(destinationLocation: Location = location, players: Collection<Player>) {
        sendLook(players = players)
        sendMount(players = players)
        PacketEntityTeleport().also {
            it.entityId = id
            it.location = destinationLocation
            it.isOnGround = isOnGround
        }.sendOrSendAll(viewers)
    }

    fun sendTeleport(destinationLocation: Location = location, vararg players: Player) =
        sendTeleport(destinationLocation, players.toList())

    fun teleport(destinationLocation: Location = location, players: Collection<Player>) {
        if (players.isEmpty()) {
            if (location.world !== destinationLocation.world)
                destroy(viewers)

            spawnLocal()

            this.location = destinationLocation.clone()
        }

        sendTeleport(destinationLocation, players)
    }

    fun teleport(destinationLocation: Location = location, vararg players: Player) =
        teleport(destinationLocation, players.toList())

    fun sendMount(mounts: List<Int> = this.mounts.map(ProtocolEntity::id), players: Collection<Player>) =
        PacketEntityMount().also {
            it.entityId = id
            it.entities = mounts.toIntArray()
        }.sendOrSendAll(players)

    fun sendMount(mounts: List<Int> = this.mounts.map(ProtocolEntity::id), vararg players: Player) =
        sendMount(mounts, players.toList())

    fun mount(vararg entities: ProtocolEntity) = apply {
        entities.forEach { it.vehicle = this }
        mounts.addAll(entities)
        syncMounts()
    }

    fun unmount(vararg entities: ProtocolEntity) = apply {
        entities.forEach { it.vehicle = null }
        mounts.removeAll(entities.toSet())
        syncMounts()
    }

    fun effect(vararg effects: ProtocolEntityEffect) = apply { this@ProtocolEntity.effects.addAll(effects) }

    fun removeEffects(vararg effects: ProtocolEntityEffect) = apply { this@ProtocolEntity.effects.removeAll(effects) }

    fun removeEffects(vararg effectsTypes: PotionEffectType) =
        removeEffects(*effectsTypes.mapNotNull { type -> effects.firstOrNull { it.type == type } }.toTypedArray())

    fun syncMounts() {
        mounts.forEach { it.location = location }
        sendMount()
    }

    fun sendLook(yaw: Float = location.yaw, pitch: Float = location.pitch, players: Collection<Player>) {
        PacketEntityHeadRotation().also {
            it.entityId = id
            it.rotationHeadYaw = yaw
        }.sendOrSendAll(players)
        PacketEntityLook().also {
            it.entityId = id
            it.rotationYaw = yaw
            it.rotationPitch = pitch
        }.sendOrSendAll(players)
    }

    fun sendLook(yaw: Float = location.yaw, pitch: Float = location.pitch, vararg players: Player) =
        sendLook(yaw, pitch, players.toList())

    fun look(yaw: Float = location.yaw, pitch: Float = location.pitch, players: Collection<Player>) {
        if ((location.yaw != yaw || location.pitch != pitch) && players.isEmpty()) {
            location.yaw = yaw
            location.pitch = pitch
        }

        sendLook(yaw, pitch, players)
    }

    fun look(yaw: Float = location.yaw, pitch: Float = location.pitch, vararg players: Player) =
        look(yaw, pitch, players.toList())

    fun look(location: Location, players: Collection<Player>) = location.clone().subtract(this.location).run {
        direction = toVector().normalize()
        look(yaw, pitch, players)
    }

    fun look(location: Location, vararg players: Player) =
        look(location, players.toList())

    fun look(player: Player) =
        look(player.location, player)

    fun sendTeam(action: ScoreboardTeamAction, players: Collection<Player>) = PacketScoreboardTeam().also {
        it.teamName = uuid.toString().replace("", "").substring(16)
        it.action = action
        it.entities = mutableListOf(uuid.toString())

        if (action == ScoreboardTeamAction.UPDATE || action == ScoreboardTeamAction.CREATE) {
            it.collision = ScoreboardTeamCollision.NEVER
            it.visibility = ScoreboardTeamTagVisibility.NEVER
            if (isGlowing) {
                it.color = glowColor
            }
        }
    }.sendOrSendAll(players)

    fun sendTeam(action: ScoreboardTeamAction, vararg players: Player) =
        sendTeam(action, players.toList())


    fun sendMetadata(items: Collection<MetadataItem<*>> = metadata.items, players: Collection<Player>) =
        PacketEntityMetadata().also {
            it.entityId = id
            it.metadataItems = items.toList()
        }.sendOrSendAll(players)

    fun sendMetadata(items: Collection<MetadataItem<*>> = metadata.items, vararg players: Player) =
        sendMetadata(items, players.toList())

    override fun remove() {
        unmount(*mounts.toTypedArray())
        vehicle?.unmount(this)
        EntityProtocol.EntitiesMap.remove(id)
        super.remove()
    }

    /*internal bc using register() is better*/
    internal fun registerEntity() {
        if (!isRegistered()) {
            EntityProtocol.EntitiesMap[id] = this
        }

        spawnLocal()
    }

    fun spawnLocal() {
        if (isRegistered()) chunk.loadedByPlayers
            .filter { hasAccess(it) && !isSpawned(it) }
            .ifNotEmpty(this::spawn)
    }

    fun refreshViewers() {
        viewers
            .filterNot(this::hasAccess)
            .ifNotEmpty(this::destroy)

        spawnLocal()
    }

    fun hasAccess(player: Player) = accessor(player)

    fun isRegistered() = this in EntityProtocol

    companion object {

        private val IdField = AtomicInteger(Int.MAX_VALUE)

        fun nextEntityId() = IdField.decrementAndGet()

    }

}

fun <E : ProtocolEntity> E.register() = apply { registerEntity() }

infix fun <E : ProtocolEntity> E.click(block: (Player, EntityInteract) -> Unit) = apply { clickHandler = block }

infix fun <E : ProtocolEntity> E.access(block: (Player) -> Boolean) = apply { accessor = block }

inline fun <E : ProtocolEntity> E.modify(batch: Boolean = true, crossinline block: E.() -> Unit) =
    metadata.modify(batch = batch) { block(this) }

inline fun <E : ProtocolEntity> E.modifySpecial(players: Collection<Player>, crossinline block: E.() -> Unit) =
    modify(false, block).apply { sendMetadata(items.values, players) }

inline fun <E : ProtocolEntity> E.modifySpecial(vararg players: Player, crossinline block: E.() -> Unit) =
    modifySpecial(players.toList(), block)

inline fun <E : ProtocolEntity> E.modifyEachViewer(crossinline block: E.(viewer: Player) -> Unit) =
    viewers.forEach { modifySpecial(it) { block(it) } }