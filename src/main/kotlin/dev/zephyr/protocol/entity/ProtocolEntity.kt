@file:Suppress("DEPRECATION")

package dev.zephyr.protocol.entity

import com.comphenix.protocol.wrappers.EnumWrappers
import com.comphenix.protocol.wrappers.WrappedChatComponent
import dev.zephyr.event.EventContext
import dev.zephyr.event.filter
import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.protocol.entity.event.PlayerFakeEntityInteractEvent
import dev.zephyr.protocol.entity.metadata.MetadataItem
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.metadata.ObservableMetadata
import dev.zephyr.protocol.entity.type.EntityDamageSource
import dev.zephyr.protocol.entity.type.EntityInteract
import dev.zephyr.protocol.packet.entity.*
import dev.zephyr.protocol.packet.entity.move.*
import dev.zephyr.protocol.packet.scoreboard.PacketScoreboardTeam
import dev.zephyr.protocol.scoreboard.ProtocolScoreboardTeam
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamAction
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamCollision
import dev.zephyr.protocol.scoreboard.type.ScoreboardTeamTagVisibility
import dev.zephyr.protocol.world.*
import dev.zephyr.util.bukkit.boundingBox
import dev.zephyr.util.bukkit.clearAngles
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.collection.ifNotEmpty
import dev.zephyr.util.collection.observe
import dev.zephyr.util.component.literal
import dev.zephyr.util.component.toComponent
import dev.zephyr.util.component.unwrap
import dev.zephyr.util.component.wrap
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.cast
import dev.zephyr.util.kotlin.observable
import dev.zephyr.util.kotlin.print
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType
import org.bukkit.util.Vector
import java.util.*
import java.util.Optional.ofNullable
import java.util.concurrent.atomic.AtomicInteger
import kotlin.jvm.optionals.getOrNull

@KotlinOpens
class ProtocolEntity(
    var entityId: Int, val uuid: UUID,
    val type: EntityType, val spawnData: Int,
    location: Location
) : ProtocolObject(), ProtocolVehicle {

    constructor(uuid: UUID, type: EntityType, spawnData: Int, location: Location) :
            this(nextEntityId(), uuid, type, spawnData, location)

    constructor(uuid: UUID, type: EntityType, location: Location) :
            this(uuid, type, 0, location)

    constructor(type: EntityType, spawnData: Int, location: Location) :
            this(UUID.randomUUID(), type, spawnData, location)

    constructor(type: EntityType, location: Location) :
            this(UUID.randomUUID(), type, 0, location)

    var clickHandler: ProtocolEntity.(Player, EntityInteract) -> Unit = { _, _ -> }
    var accessor: (Player) -> Boolean = { true }

    val effects =
        concurrentSetOf<ProtocolEntityEffect>().observe({ it.add(entityId, viewers) }, { it.remove(entityId, viewers) })
    override val mounts = mutableSetOf<ProtocolEntity>()
    var vehicle: ProtocolVehicle? = null

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
    var customName: String?
        get() = customNameComponent?.literal()
        set(value) {
            customNameComponent = value?.toComponent()
        }
    var customNameComponent: Component?
        get() = metadata.get<Optional<WrappedChatComponent>>(2)?.value?.getOrNull()?.unwrap()
        set(value) {
            metadata[2] = MetadataType.ChatOptional.newItem(2, ofNullable(value?.wrap()))
            isCustomNameVisible = value != null
        }

    var isCustomNameVisible by metadata.item(3, MetadataType.Boolean, false)
    var isSilent by metadata.item(4, MetadataType.Boolean, false)
    var isNoGravity by metadata.item(5, MetadataType.Boolean, false)
    var pose by metadata.item(6, MetadataType.Pose, EnumWrappers.EntityPose.STANDING)
    var ticksFrozen by metadata.item(7, MetadataType.Int, 0)
    var isOnGround = true

    val boundingBox = type.boundingBox
    val positionBox get() = boundingBox.clone().shift(location)

    var worldIsDirty = false
    var latestWorldSnapshot = location.world

    var chunkIsDirty = false
    var latestChunkSnapshot = location.position.chunk

    var location by observable(location) { old, new ->
        if (!worldIsDirty) {
            worldIsDirty = latestWorldSnapshot != new.world
        }
        if (!chunkIsDirty && !worldIsDirty) {
            chunkIsDirty = latestChunkSnapshot != new.position.chunk
        }
        spawnLocal()
    }

    val chunkPosition get() = location.position.chunk

    val chunk get() = location.chunk
    val world get() = location.world

    override fun spawn(players: Collection<Player>) {
        super.spawn(players)
        team.spawn(players)
    }

    override fun sendSpawnPackets(players: Collection<Player>) {
        sendSpawn(players)
        sendLook(players = players)
        sendMetadata(players = players)

        sendEffects(players = players)
        sendMounts(players = players)
        vehicle?.sendMounts(players = players)
    }

    fun sendSpawn(players: Collection<Player>) = PacketEntitySpawn().also {
        it.entityId = entityId
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
        it.entitiesIds = listOf(entityId)
    }.sendOrSendAll(players)

    fun sendDestroy(vararg players: Player) =
        sendDestroy(players.toList())

    fun sendStatus(status: Int, players: Collection<Player>) = PacketEntityStatus().also {
        it.entityId = entityId
        it.status = status.toByte()
    }.sendOrSendAll(players)


    /**
    https://wiki.vg/Entity_statuses
     **/
    fun sendStatus(status: Int, vararg players: Player) =
        sendStatus(status, players.toList())

    fun damage(source: EntityDamageSource = EntityDamageSource.HURT, players: Collection<Player>) =
        PacketEntityDamageEvent().also {
            it.entityId = entityId
            it.sourceType = source
        }.sendOrSendAll(players)

    fun damage(source: EntityDamageSource = EntityDamageSource.HURT, vararg players: Player) =
        damage(source, players.toList())

    fun sendEffects(effects: Collection<ProtocolEntityEffect> = this.effects, players: Collection<Player>) =
        effects.forEach { it.add(entityId, players) }

    fun sendEffects(effects: Collection<ProtocolEntityEffect> = this.effects, vararg players: Player) =
        sendEffects(effects, players.toList())

    fun sendRemoveEffects(effects: Collection<ProtocolEntityEffect> = this.effects, players: Collection<Player>) =
        effects.forEach { it.remove(entityId, players) }

    fun sendRemoveEffects(effects: Collection<ProtocolEntityEffect> = this.effects, vararg players: Player) =
        sendRemoveEffects(effects, players.toList())

    fun sendMove(vector: Vector, players: Collection<Player>) = PacketEntityMove().also {
        it.entityId = entityId
        it.motion = vector
        it.isOnGround = isOnGround
    }.sendOrSendAll(players)

    fun sendMove(vector: Vector, vararg players: Player) =
        sendMove(vector, players.toList())

    fun move(vector: Vector, players: Collection<Player>) {
        if (players.isEmpty()) {
            location = location.add(vector)
        }

        sendMove(vector, players)
    }

    fun move(vector: Vector, vararg players: Player) =
        move(vector, players.toList())

    fun sendMove(vector: Vector, yaw: Float, pitch: Float, players: Collection<Player>) = PacketEntityMoveLook().also {
        it.entityId = entityId
        it.motion = vector
        it.yaw = yaw
        it.pitch = pitch
        it.isOnGround = isOnGround
    }.sendOrSendAll(players)

    fun sendMove(vector: Vector, yaw: Float, pitch: Float, vararg players: Player) =
        sendMove(vector, yaw, pitch, players.toList())

    fun move(vector: Vector, yaw: Float, pitch: Float, players: Collection<Player>) {
        if (players.isEmpty()) {
            location.yaw = yaw
            location.pitch = pitch
            location = location.add(vector)
        }

        sendMove(vector, yaw, pitch, players)
    }

    fun move(vector: Vector, yaw: Float, pitch: Float, vararg players: Player) =
        move(vector, yaw, pitch, players.toList())

    fun sendVelocity(velocity: Vector, players: Collection<Player>) = PacketEntityVelocity().also {
        it.entityId = entityId
        it.velocity = velocity
    }.sendOrSendAll(players)

    fun sendVelocity(velocity: Vector, vararg players: Player) =
        sendVelocity(velocity, players.toList())


    fun sendTeleport(destinationLocation: Location = location, players: Collection<Player>) {
        sendLook(players = players)
        sendMounts(players = players)
        PacketEntityTeleport().also {
            it.entityId = entityId
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

    @Synchronized
    override fun sendMounts(players: Collection<Player>) =
        PacketEntityMount().also {
            it.entityId = entityId
            it.entities = this.mounts.map(ProtocolEntity::entityId).toIntArray()
        }.sendOrSendAll(players)

    fun sendMounts(vararg players: Player) =
        sendMounts(players.toList())

    @Synchronized
    override fun mount(entities: Collection<ProtocolEntity>) {
        entities.forEach { it.vehicle = this }
        mounts.addAll(entities)
        syncMounts()
        sendMounts()
    }

    @Synchronized
    override fun unmount(entities: Collection<ProtocolEntity>) {
        entities.forEach { it.vehicle = null }
        mounts.removeAll(entities.toSet())
        syncMounts()
        sendMounts()
    }

    @Synchronized
    override fun syncMounts() {
        mounts.forEach { it.location = location.clearAngles() }
    }

    fun effect(vararg effects: ProtocolEntityEffect) = apply { this@ProtocolEntity.effects.addAll(effects) }

    fun removeEffects(vararg effects: ProtocolEntityEffect) = apply { this@ProtocolEntity.effects.removeAll(effects) }

    fun removeEffects(vararg effectsTypes: PotionEffectType) =
        removeEffects(*effectsTypes.mapNotNull { type -> effects.firstOrNull { it.type == type } }.toTypedArray())

    fun sendLook(yaw: Float = location.yaw, pitch: Float = location.pitch, players: Collection<Player>) {
        PacketEntityHeadRotation().also {
            it.entityId = entityId
            it.rotationHeadYaw = yaw
        }.sendOrSendAll(players)
        PacketEntityLook().also {
            it.entityId = entityId
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
            it.entityId = entityId
            it.metadataItems = items.toList()
        }.sendOrSendAll(players)

    fun sendMetadata(items: Collection<MetadataItem<*>> = metadata.items, vararg players: Player) =
        sendMetadata(items, players.toList())

    override fun remove() {
        unmount(*mounts.toTypedArray())
        vehicle?.unmount(this)
        EntityProtocol.Entities.remove(entityId)
        EntityProtocol.EntitiesByPositions[world]?.get(latestChunkSnapshot)?.remove(entityId)
        super.remove()
    }

    override fun filterEvents(ctx: EventContext) {
        ctx.filter<PlayerFakeEntityInteractEvent> { it.entity === this }
    }

    internal fun registerEntity() {
        if (!isRegistered()) {
            EntityProtocol.Entities[entityId] = this
            EntityProtocol.EntitiesByPositions.getOrPut(world) {
                concurrentHashMapOf()
            }.getOrPut(location.position.chunk) {
                concurrentHashMapOf()
            }[entityId] = this
        }
        PlayerChunks.PlayersLoadedChunks
            .filter {
                location.world == it.key.world
                        && location.position.chunk in it.value
                        && !isSpawned(it.key)
                        && !isLoaded(it.key)
                        && hasAccess(it.key)
            }.keys
            .also(::load)
            .also(::spawn)
    }

    fun spawnLocal() {
        if (isRegistered()) loaders
            .filter { !isSpawned(it) && hasAccess(it) }
            .ifNotEmpty(this::spawn)
    }

    fun refreshViewers() {
        viewers
            .filter { !isLoaded(it) || !hasAccess(it) }
            .ifNotEmpty(this::destroy)

        spawnLocal()
    }

    fun refreshLoaders() {
        val chunk = latestChunkSnapshot
        loaders.filter { chunk !in PlayerChunks[it] }.ifNotEmpty(this::unload)
        world.players.filter { !isLoaded(it) && chunk in PlayerChunks[it] }.ifNotEmpty(this::load)
    }

    fun hasAccess(player: Player) = accessor(player)

    fun isRegistered() = this in EntityProtocol

    companion object {

        private val IdField = AtomicInteger(Int.MAX_VALUE)

        fun nextEntityId() = IdField.decrementAndGet()

    }

}

fun <E : ProtocolEntity> E.register() = apply { registerEntity() }

infix fun <E : ProtocolEntity> E.click(block: E.(Player, EntityInteract) -> Unit) =
    apply { clickHandler = block.cast() }

infix fun <E : ProtocolEntity> E.access(block: (Player) -> Boolean) = apply { accessor = block }

inline fun <E : ProtocolEntity> E.modify(batch: Boolean = true, crossinline block: E.() -> Unit) =
    metadata.modify(batch = batch) { block(this) }

inline fun <E : ProtocolEntity> E.modifySpecial(players: Collection<Player>, crossinline block: E.() -> Unit) =
    modify(false, block).apply { sendMetadata(items.values, players) }

inline fun <E : ProtocolEntity> E.modifySpecial(vararg players: Player, crossinline block: E.() -> Unit) =
    modifySpecial(players.toList(), block)

inline fun <E : ProtocolEntity> E.modifyEachViewer(crossinline block: E.(viewer: Player) -> Unit) =
    viewers.forEach { modifySpecial(it) { block(it) } }

fun ProtocolEntity.pickUp(entityId: Int, amount: Int = 1) = PacketPickUpEntity().also {
    it.entityId = this@pickUp.entityId
    it.collectorEntityId = entityId
    it.amount = amount
}.send(viewers)

fun ProtocolEntity.pickUp(entity: Entity, amount: Int = 1) = pickUp(entity.entityId)