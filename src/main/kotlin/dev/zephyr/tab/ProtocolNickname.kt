package dev.zephyr.tab

import dev.zephyr.protocol.entity.type.display.DisplayBillBoard
import dev.zephyr.protocol.entity.world.display.ProtocolTextDisplay
import dev.zephyr.protocol.entity.world.display.removeShadow
import dev.zephyr.util.component.toComponent
import org.bukkit.entity.Player
import org.joml.Vector3f

class ProtocolNickname(val player: Player) : ProtocolTextDisplay(player.location) {
    var onSneakAction = {
        isSeeThrough = false
        textOpacity = 63
    }

    var offSneakAction = {
        isSeeThrough = true
        textOpacity = -1
    }

    init {
        isSeeThrough = true
        isShadowed = true
        text = player.name.toComponent()
        translation = Vector3f(0f, .6f, 0f)
        billboard = DisplayBillBoard.VERTICAL
        scale = Vector3f(1f)
        removeBackground()
        removeShadow()
    }
}