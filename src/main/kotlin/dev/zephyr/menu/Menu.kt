package dev.zephyr.menu

import dev.zephyr.event.GlobalEventContext
import dev.zephyr.event.on
import dev.zephyr.menu.draw.DelegateMenuDrawer
import dev.zephyr.menu.draw.MenuDrawer
import dev.zephyr.menu.icon.MenuIcon
import dev.zephyr.menu.setting.MenuSetting
import dev.zephyr.menu.setting.MenuType
import dev.zephyr.menu.updater.MenuUpdater
import dev.zephyr.menu.updater.update
import dev.zephyr.util.bukkit.cancel
import dev.zephyr.util.bukkit.forceInMainThread
import dev.zephyr.util.collection.concurrentHashMapOf
import dev.zephyr.util.collection.concurrentSetOf
import dev.zephyr.util.kotlin.KotlinOpens
import dev.zephyr.util.kotlin.cast
import net.kyori.adventure.text.Component
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

@KotlinOpens
class Menu(val setting: MenuSetting) : InventoryHolder {

    val events = GlobalEventContext.fork(false).filter { it is InventoryEvent && it.inventory.holder === this }

    lateinit var viewer: Player

    private var drawed = false
    private var cachedInventory: Inventory? = null
    override fun getInventory() = cachedInventory!!

    val icons = concurrentHashMapOf<Int, MenuIcon>()
    val updaters = concurrentSetOf<MenuUpdater<Menu>>()

    val drawers = concurrentSetOf<MenuDrawer<Menu>>()

    init {
        events.on<InventoryClickEvent> {
            if (!setting.editable) cancel()
            icons[slot]?.onClick(this@Menu, this)
        }
    }

    //items
    operator fun set(slot: Int, icon: MenuIcon) {
        icons[slot] = icon
        if (drawed) cachedInventory?.setItem(slot, icon.itemStack)
    }

    operator fun set(x: Int, y: Int, icon: MenuIcon) {
        icons[y * setting.columns + x] = icon
        if (drawed) cachedInventory?.setItem(y * setting.columns + x, icon.itemStack)
    }


    operator fun get(slot: Int) = icons[slot]

    operator fun get(x: Int, y: Int) = icons[y * setting.columns + x]

    fun remove(slot: Int) = icons.remove(slot)

    fun remove(x: Int, y: Int) = icons.remove(y * setting.columns + x)

    fun remove(icon: MenuIcon) = icons.values.removeIf { it == icon }

    operator fun MenuIcon.unaryPlus() = set(defaultSlot, this)

    operator fun MenuIcon.unaryMinus() = remove(this)

    //drawers
    operator fun MenuDrawer<*>.unaryPlus() = drawer(this)

    fun drawer(drawer: MenuDrawer<*>) = apply { drawers.add(drawer.cast()) }

    fun tick() {
        updaters.update(this)
        icons.forEach { (slot, item) ->
            item.onUpdate(this)
            if (item.dirty) {
                item.dirty = false
                inventory.setItem(slot, item.itemStack)
            }
        }
    }

    fun open(player: Player) {
        this.viewer = player
        cachedInventory = setting.createInventory(this)

        draw()
        forceInMainThread { player.openInventory(inventory) }
        MenuService.add(this)
    }

    fun draw() {
        drawed = false
        icons.clear()
        drawers.forEach { it.draw(this) }

        icons.forEach { (slot, icon) -> inventory.setItem(slot, icon.itemStack) }
        drawed = true
    }

    fun reopen() {
        open(viewer)
    }

    fun close(fromEvent: Boolean = false) {
        if (!fromEvent) {
            cachedInventory?.close()
            viewer.closeInventory()
        }
        events.close()
        MenuService.remove(this)
    }

}

fun <M : Menu> M.updater(
    delay: Int = 20,
    period: Int = 20,
    repeats: Int = -1,
    block: Menu.() -> Unit
) = apply { updaters.add(MenuUpdater(delay, period, repeats) { block() }) }

fun <M : Menu> M.update(block: Menu.() -> Unit = {}) {
    block()
    reopen()
}

infix fun <M : Menu> M.draw(handler: M.() -> Unit) = drawer(DelegateMenuDrawer(handler))

fun menu(setting: MenuSetting, block: Menu.() -> Unit) = Menu(setting).apply(block)

fun menu(title: Component, rows: Int, block: Menu.() -> Unit) = menu(MenuSetting(title, rows), block)

fun menu(title: Component, type: MenuType, block: Menu.() -> Unit) = menu(MenuSetting(title, type), block)