package dev.zephyr.protocol.entity.world.display

import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.data.display.*
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.protocol.entity.type.display.DisplayBillBoard
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.observable
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.joml.Quaternionf
import org.joml.Vector3f

@KotlinOpens
class ProtocolDisplay(location: Location, type: EntityType) : ProtocolEntity(type, location) {

    init {
        metadata.writeDefaults()
    }

    var interpolation: DisplayInterpolation<*> by observable(DisplayInterpolation.NOOP) { old, _ -> cancelInterpolation(old) }

    var interpolationDelay by metadata.item(8, MetadataType.Int, 0)
    var interpolationDuration by metadata.item(9, MetadataType.Int, 0)

    var translation by metadata.item(10, MetadataType.JomlVector, Vector3f(0f, 0f, 0f))
    var scale by metadata.item(11, MetadataType.JomlVector, Vector3f(1f, 1f, 1f))

    var leftRotation by metadata.item(12, MetadataType.Quaternion, Quaternionf(0f, 0f, 0f, 1f))
    var rightRotation by metadata.item(13, MetadataType.Quaternion, Quaternionf(0f, 0f, 0f, 1f))

    var billboard by metadata.item(14, MetadataType.Byte, DisplayBillBoard.FIXED, DisplayBillBoard::id)
    var brightness by metadata.item(15, MetadataType.Int, Brightness.Default, Brightness::value)

    var viewRange by metadata.item(16, MetadataType.Float, 1f)
    var shadowRadius by metadata.item(17, MetadataType.Float, 1f)
    var shadowStrength by metadata.item(18, MetadataType.Float, 1f)

    var width by metadata.item(19, MetadataType.Float, 0f)
    var height by metadata.item(20, MetadataType.Float, 0f)

    var glowColorOverride by metadata.item(21, MetadataType.Int, -1)

    fun cancelInterpolation(runningInterpolation: DisplayInterpolation<*> = interpolation) {
        if (runningInterpolation.isRunning) {
            runningInterpolation.cancel()
        }
    }

    override fun handleRemove() {
        super.handleRemove()

        interpolation.cancel()
    }

}

inline fun <D : ProtocolDisplay> D.interpolate(
    delay: Long, duration: Long, crossinline block: D.() -> Unit
): DisplayInterpolation<D> = DisplayDelayedInterpolation(this, delay, duration) { block(this) }
    .apply(DisplayInterpolation<*>::runIfNoPreviousInterpolation)

inline fun <D : ProtocolDisplay> D.interpolateCycle(
    delay: Long, duration: Long, cycles: Int = -1,
    crossinline block: D.() -> Unit
): DisplayInterpolation<D> = DisplayCycleInterpolation(this, delay, duration, cycles) { block(this) }
    .apply(DisplayInterpolation<*>::runIfNoPreviousInterpolation)

inline fun <D : ProtocolDisplay> D.interpolateSwitch(
    delay: Long, duration: Long, cycles: Int = -1,
    crossinline block: DisplaySwitchableInterpolation.(D) -> Unit
): DisplayInterpolation<D> = DisplayCycleInterpolation(
    this, delay, duration, cycles,
    DisplaySwitchableInterpolation().also { block(it, this) }
).apply(DisplayInterpolation<*>::runIfNoPreviousInterpolation)


fun <D : ProtocolDisplay> D.removeShadow() = apply {
    shadowRadius = 0f
    shadowStrength = 0f
}

fun <D : ProtocolDisplay> D.fullBright() = apply {
    brightness = Brightness.Max
}

fun <D : ProtocolDisplay> D.maxViewRange() = apply {
    viewRange = Float.MAX_VALUE
}



