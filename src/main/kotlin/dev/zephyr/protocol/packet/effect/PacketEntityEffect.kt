package dev.zephyr.protocol.packet.effect

import com.comphenix.protocol.events.InternalStructure
import dev.zephyr.protocol.PacketPlayOutType
import dev.zephyr.protocol.packet.ProtocolPacket
import dev.zephyr.util.java.Unsafe
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import kotlin.experimental.or

@KotlinOpens
class PacketEntityEffect : ProtocolPacket(PacketPlayOutType.ENTITY_EFFECT) {

    var entityId by writer(0, integers)
    var effectType by writer(0, effectTypes)

    var level by writer(0, bytes)
    var duration by writer(1, integers)

    var flags by observable(0.toByte()) { _, flags -> bytes.write(1, flags) }

    var isAmbient by writer<Boolean> { flags = flags or 0x01 }
    var isShowParticles by writer<Boolean> { flags = flags or 0x02 }
    var isShowIcon by writer<Boolean> { flags = flags or 0x04 }

    val structure by lazy {
        InternalStructure.getConverter().getSpecific(Unsafe.allocateInstance(structures.getField(1).type))
            .also { structures.write(1, it) }
    }

    var paddingDuration by writer<Int> { structure.integers.write(0, it) }
    var factorStart by writer<Float> { structure.float.write(0, it) }
    var factorTarget by writer<Float> { structure.float.write(1, it) }
    var factorCurrent by writer<Float> { structure.float.write(2, it) }
    var ticksAlive by writer<Int> { structure.integers.write(1, it) }
    var factorPreviousFrame by writer<Float> { structure.float.write(3, it) }
    var hadEffectLastTick by writer<Boolean> { structure.booleans.write(0, it) }

}