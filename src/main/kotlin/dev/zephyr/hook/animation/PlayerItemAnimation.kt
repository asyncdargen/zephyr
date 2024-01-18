package dev.zephyr.hook.animation

import dev.zephyr.hook.vehicle.mount
import dev.zephyr.protocol.entity.mob.animal.ProtocolTurtle
import dev.zephyr.protocol.entity.type.display.DisplayBillBoard
import dev.zephyr.protocol.entity.world.display.*
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.joml.Quaternionf
import org.joml.Vector3f

@KotlinOpens
class PlayerItemAnimation(val player: Player, item: ItemStack) : ProtocolItemDisplay(player.location, item) {

    val displaySlot = ProtocolTurtle(location).apply {
        isBaby = true
        isInvisible = true
        player.mount(this)
    }

    init {
        fullBright()
        removeShadow()
        maxViewRange()

        scale = Vector3f(0f, 0f, 0f)
        translation = Vector3f(0f, .025f, -.1f)
        billboard = DisplayBillBoard.CENTER

        displaySlot.mount(this)
        displaySlot.spawn(player)

        spawn(player)

        animate()
    }

    fun animate() {
        interpolate(1, 7) {
            scaleUp()
            rollAroundFront()
        } next {
            interpolate(0, 13) {
                waveLeft()
            }
        } next {
            interpolate(0, 13) {
                waveRight()
            }
        } next {
            interpolate(0, 7) {
                scaleDown()
                rollAroundBack()
            }
        } after {
            stop()
        }
    }

    fun stop() {
        interpolation.cancel()

        remove()
        displaySlot.remove()
    }

    private fun waveLeft() {
        translation = Vector3f(0f, .025f, -.1f)

        leftRotation = Quaternionf(0.07f, -1f, 0f, 0f)
    }

    private fun waveRight() {
        translation = Vector3f(0.04f, .025f, -.1f)

        leftRotation = Quaternionf(-0.05f, -1f, 0f, 0f)
    }

    private fun scaleUp() {
        scale = Vector3f(.08f, .08f, .08f)
    }

    private fun scaleDown() {
        scale = Vector3f(0f, 0f, 0f)
    }

    private fun rollAroundFront() {
        translation = Vector3f(0.025f, .025f, -.1f)

        rightRotation = rightRotation.rotationY(-3f)
        leftRotation = leftRotation.rotationY(-3f)

    }

    private fun rollAroundBack() {
        translation = Vector3f(0f, .025f, -.1f)

        rightRotation = rightRotation.rotationY(6f)
        leftRotation = leftRotation.rotationY(6f)
    }

}
