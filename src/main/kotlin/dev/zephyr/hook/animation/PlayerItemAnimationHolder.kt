package dev.zephyr.hook.animation

import com.google.common.cache.CacheBuilder
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.concurrent.TimeUnit

object PlayerItemAnimationHolder {

    private val itemAnimations = CacheBuilder.newBuilder()
        .expireAfterWrite(2050, TimeUnit.MILLISECONDS)
        .build<Player, PlayerItemAnimation>()

    fun showItem(player: Player, item: ItemStack) =
        itemAnimations.asMap().put(player, PlayerItemAnimation(player, item))?.remove()

}

fun Player.showItem(item: ItemStack) = PlayerItemAnimationHolder.showItem(this, item)