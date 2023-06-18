package dev.zephyr.protocol.entity.metadata

import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.wrappers.*
import com.comphenix.protocol.wrappers.EnumWrappers.*
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Serializer
import com.comphenix.protocol.wrappers.nbt.NbtCompound
import dev.zephyr.protocol.entity.data.villager.VillagerData
import dev.zephyr.protocol.entity.type.cat.CatVariant
import dev.zephyr.protocol.entity.type.frog.FrogVariant
import dev.zephyr.protocol.entity.type.painting.ArtVariant
import dev.zephyr.protocol.entity.type.sniffer.SnifferState
import dev.zephyr.util.java.*
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.inventory.ItemStack
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.*
import kotlin.reflect.KClass

@KotlinOpens
class MetadataType<T>(val serializer: Serializer, val converter: ((T) -> Any)? = null) {

    companion object {

        val Boolean = MetadataType<Boolean>(serializer(JBoolean::class))
        val Byte = MetadataType<Byte>(serializer(JByte::class))
        val Int = MetadataType<Int>(serializer(JInt::class))
        val IntOptional = MetadataType<OptionalInt>(serializer(OptionalInt::class.java))
        val Long = MetadataType<Int>(serializer(JLong::class))
        val Float = MetadataType<Float>(serializer(JFloat::class))
        val String = MetadataType<String>(serializer(JString::class))
        val ItemStack = MetadataType<ItemStack>(Registry.getItemStackSerializer(false))

        val BlockPosition = MetadataType<BlockPosition>(Registry.getBlockPositionSerializer(false))
        val BlockPositionOptional = MetadataType<Optional<BlockPosition>>(Registry.getBlockPositionSerializer(true))

        val ChatComponent = MetadataType<WrappedChatComponent>(Registry.getChatComponentSerializer(false))
        val ChatComponentOptional = MetadataType<Optional<WrappedChatComponent>>(Registry.getChatComponentSerializer(true))

        val BlockData = MetadataType<WrappedBlockData>(Registry.getBlockDataSerializer(false))
        val BlockDataOptional = MetadataType<Optional<WrappedBlockData>>(Registry.getBlockDataSerializer(true))

        val UUIDOptional = MetadataType<Optional<UUID>>(Registry.getBlockDataSerializer(true))

        val NBT = MetadataType<NbtCompound>(Registry.getNBTCompoundSerializer())
        val VillagerData = MetadataType<VillagerData>(serializer(WrappedVillagerData.getNmsClass()))

        //todo: fix it
//        val ArtVariant = MetadataType<ArtVariant>(serializer(Holder::class.java))
        val CatVariant = MetadataType<CatVariant>(serializer(net.minecraft.world.entity.animal.CatVariant::class))
        val FrogVariant = MetadataType<FrogVariant>(serializer(net.minecraft.world.entity.animal.FrogVariant::class))

        val SnifferState = MetadataType<SnifferState>(serializer(Class.forName("net.minecraft.world.entity.animal.sniffer.Sniffer\$a")))

        val Particle = MetadataType<WrappedParticle<*>>(serializer(MinecraftReflection.getParticleParam()))
        val Direction = MetadataType<Direction>(Registry.getDirectionSerializer())
        val Pose = MetadataType<EntityPose>(serializer(getEntityPoseClass()))

        val Vector = MetadataType<Vector3F>(serializer(Vector3F.getMinecraftClass()))
        val JomlVector = MetadataType<Vector3f>(serializer(Vector3f::class))
        val Quaternion = MetadataType<Quaternionf>(serializer(Quaternionf::class))

        fun unwrap(wrapped: Any): Any? {
            return when (wrapped) {
                is Optional<*> -> wrapped.map { unwrap(it) }
                is ItemStack -> BukkitConverters.getItemStackConverter().getGeneric(wrapped)
                is WrappedBlockData -> BukkitConverters.getWrappedBlockDataConverter().getGeneric(wrapped)
                is Vector3F -> Vector3F.getConverter().getGeneric(wrapped)
                is BlockPosition -> com.comphenix.protocol.wrappers.BlockPosition.getConverter().getGeneric(wrapped)
                is Direction -> getDirectionConverter().getGeneric(wrapped)
                is EntityPose -> getEntityPoseConverter().getGeneric(wrapped)
                is AbstractWrapper -> wrapped.handle
                is FrogVariant -> wrapped.handle
                is CatVariant -> wrapped.handle
                is SnifferState -> wrapped.handle
                is ArtVariant -> wrapped.handle
                else -> wrapped
            }
        }

        private fun serializer(declaredClass: KClass<*>, optional: Boolean = false) =
            serializer(declaredClass.java, optional)

        private fun serializer(declaredClass: Class<*>, optional: Boolean = false) =
            Registry.get(declaredClass, optional)

    }

    fun newItem(index: Int, value: T?) = MetadataItem(index, this, value)

}