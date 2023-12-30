package dev.zephyr.protocol.entity

import dev.zephyr.protocol.entity.mob.ProtocolAllay
import dev.zephyr.protocol.entity.mob.ProtocolSlime
import dev.zephyr.protocol.entity.mob.ambient.ProtocolBat
import dev.zephyr.protocol.entity.mob.animal.*
import dev.zephyr.protocol.entity.mob.animal.cow.ProtocolCow
import dev.zephyr.protocol.entity.mob.animal.cow.ProtocolMooshroom
import dev.zephyr.protocol.entity.mob.animal.horse.*
import dev.zephyr.protocol.entity.mob.animal.llama.ProtocolLlama
import dev.zephyr.protocol.entity.mob.animal.llama.ProtocolTraderLlama
import dev.zephyr.protocol.entity.mob.animal.tameable.ProtocolCat
import dev.zephyr.protocol.entity.mob.animal.tameable.ProtocolParrot
import dev.zephyr.protocol.entity.mob.animal.tameable.ProtocolWolf
import dev.zephyr.protocol.entity.mob.animal.villager.ProtocolVillager
import dev.zephyr.protocol.entity.mob.animal.villager.ProtocolWanderingTrader
import dev.zephyr.protocol.entity.mob.boss.ProtocolEnderDragon
import dev.zephyr.protocol.entity.mob.boss.ProtocolWither
import dev.zephyr.protocol.entity.mob.flying.ProtocolGhast
import dev.zephyr.protocol.entity.mob.flying.ProtocolPhantom
import dev.zephyr.protocol.entity.mob.golem.ProtocolIronGolem
import dev.zephyr.protocol.entity.mob.golem.ProtocolShulker
import dev.zephyr.protocol.entity.mob.golem.ProtocolSnowGolem
import dev.zephyr.protocol.entity.mob.monster.*
import dev.zephyr.protocol.entity.mob.monster.guadian.ProtocolElderGuardian
import dev.zephyr.protocol.entity.mob.monster.guadian.ProtocolGuardian
import dev.zephyr.protocol.entity.mob.monster.piglin.ProtocolPiglin
import dev.zephyr.protocol.entity.mob.monster.piglin.ProtocolPiglinBrute
import dev.zephyr.protocol.entity.mob.monster.raider.ProtocolRavager
import dev.zephyr.protocol.entity.mob.monster.raider.ProtocolWitch
import dev.zephyr.protocol.entity.mob.monster.raider.illager.ProtocolEvoker
import dev.zephyr.protocol.entity.mob.monster.raider.illager.ProtocolIllusioner
import dev.zephyr.protocol.entity.mob.monster.raider.illager.ProtocolPillager
import dev.zephyr.protocol.entity.mob.monster.raider.illager.ProtocolVindicator
import dev.zephyr.protocol.entity.mob.monster.skeleton.ProtocolSkeleton
import dev.zephyr.protocol.entity.mob.monster.skeleton.ProtocolStray
import dev.zephyr.protocol.entity.mob.monster.skeleton.ProtocolWitherSkeleton
import dev.zephyr.protocol.entity.mob.monster.spider.ProtocolCaveSpider
import dev.zephyr.protocol.entity.mob.monster.spider.ProtocolSpider
import dev.zephyr.protocol.entity.mob.monster.zombie.*
import dev.zephyr.protocol.entity.mob.water.ProtocolDolphin
import dev.zephyr.protocol.entity.mob.water.ProtocolGlowSquid
import dev.zephyr.protocol.entity.mob.water.ProtocolSquid
import dev.zephyr.protocol.entity.mob.water.fish.*
import dev.zephyr.protocol.entity.world.*
import dev.zephyr.protocol.entity.world.arrow.ProtocolArrow
import dev.zephyr.protocol.entity.world.arrow.ProtocolSpectralArrow
import dev.zephyr.protocol.entity.world.boat.ProtocolBoat
import dev.zephyr.protocol.entity.world.boat.ProtocolChestBoat
import dev.zephyr.protocol.entity.world.display.ProtocolBlockDisplay
import dev.zephyr.protocol.entity.world.display.ProtocolInteraction
import dev.zephyr.protocol.entity.world.display.ProtocolItemDisplay
import dev.zephyr.protocol.entity.world.display.ProtocolTextDisplay
import dev.zephyr.protocol.entity.world.fireball.ProtocolDragonFireball
import dev.zephyr.protocol.entity.world.fireball.ProtocolFireball
import dev.zephyr.protocol.entity.world.fireball.ProtocolSmallFireball
import dev.zephyr.protocol.entity.world.frame.ProtocolGlowingItemFrame
import dev.zephyr.protocol.entity.world.frame.ProtocolItemFrame
import dev.zephyr.protocol.entity.world.living.ProtocolArmorStand
import dev.zephyr.protocol.entity.world.living.ProtocolPlayer
import dev.zephyr.protocol.entity.world.minecart.*
import dev.zephyr.protocol.entity.world.thrown.*
import dev.zephyr.util.kotlin.cast
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import org.bukkit.Location
import org.bukkit.entity.EntityType

typealias Spawner = (Location) -> ProtocolEntity

object ProtocolEntitySpawner {

    val Class2Type: MutableMap<Class<out ProtocolEntity>, EntityType> = Object2ObjectOpenHashMap()
    val Class2Spawner: MutableMap<Class<out ProtocolEntity>, Spawner> = Object2ObjectOpenHashMap()
    val Type2Spawner: MutableMap<EntityType, Spawner> = Object2ObjectOpenHashMap()

    init {
        spawner(EntityType.BAT, ::ProtocolBat)
        spawner(EntityType.COW, ::ProtocolCow)
        spawner(EntityType.MUSHROOM_COW, ::ProtocolMooshroom)
        spawner(EntityType.DONKEY, ::ProtocolDonkey)
        spawner(EntityType.HORSE, ::ProtocolHorse)
        spawner(EntityType.MULE, ::ProtocolMule)
        spawner(EntityType.SKELETON_HORSE, ::ProtocolSkeletonHorse)
        spawner(EntityType.ZOMBIE_HORSE, ::ProtocolZombieHorse)
        spawner(EntityType.LLAMA, ::ProtocolLlama)
        spawner(EntityType.TRADER_LLAMA, ::ProtocolTraderLlama)
        spawner(EntityType.AXOLOTL, ::ProtocolAxolotl)
        spawner(EntityType.BEE, ::ProtocolBee)
        spawner(EntityType.CHICKEN, ::ProtocolChicken)
        spawner(EntityType.FOX, ::ProtocolFox)
        spawner(EntityType.FROG, ::ProtocolFrog)
        spawner(EntityType.GOAT, ::ProtocolGoat)
        spawner(EntityType.HOGLIN, ::ProtocolHoglin)
        spawner(EntityType.OCELOT, ::ProtocolOcelot)
        spawner(EntityType.PANDA, ::ProtocolPanda)
        spawner(EntityType.PIG, ::ProtocolPig)
        spawner(EntityType.POLAR_BEAR, ::ProtocolPolarBear)
        spawner(EntityType.RABBIT, ::ProtocolRabbit)
        spawner(EntityType.SHEEP, ::ProtocolSheep)
        spawner(EntityType.STRIDER, ::ProtocolStrider)
        spawner(EntityType.TURTLE, ::ProtocolTurtle)
        spawner(EntityType.CAT, ::ProtocolCat)
        spawner(EntityType.PARROT, ::ProtocolParrot)
        spawner(EntityType.WOLF, ::ProtocolWolf)
        spawner(EntityType.VILLAGER, ::ProtocolVillager)
        spawner(EntityType.WANDERING_TRADER, ::ProtocolWanderingTrader)
        spawner(EntityType.ENDER_DRAGON, ::ProtocolEnderDragon)
        spawner(EntityType.WITHER, ::ProtocolWither)
        spawner(EntityType.GHAST, ::ProtocolGhast)
        spawner(EntityType.PHANTOM, ::ProtocolPhantom)
        spawner(EntityType.IRON_GOLEM, ::ProtocolIronGolem)
        spawner(EntityType.SHULKER, ::ProtocolShulker)
        spawner(EntityType.SNOWMAN, ::ProtocolSnowGolem)
        spawner(EntityType.ELDER_GUARDIAN, ::ProtocolElderGuardian)
        spawner(EntityType.GUARDIAN, ::ProtocolGuardian)
        spawner(EntityType.PIGLIN, ::ProtocolPiglin)
        spawner(EntityType.PIGLIN_BRUTE, ::ProtocolPiglinBrute)
        spawner(EntityType.BLAZE, ::ProtocolBlaze)
        spawner(EntityType.CREEPER, ::ProtocolCreeper)
        spawner(EntityType.ENDERMAN, ::ProtocolEnderman)
        spawner(EntityType.ENDERMITE, ::ProtocolEndermite)
        spawner(EntityType.GIANT, ::ProtocolGiant)
        spawner(EntityType.SILVERFISH, ::ProtocolSilverfish)
        spawner(EntityType.VEX, ::ProtocolVex)
        spawner(EntityType.WARDEN, ::ProtocolWarden)
        spawner(EntityType.ZOGLIN, ::ProtocolZoglin)
        spawner(EntityType.EVOKER, ::ProtocolEvoker)
        spawner(EntityType.ILLUSIONER, ::ProtocolIllusioner)
        spawner(EntityType.PILLAGER, ::ProtocolPillager)
        spawner(EntityType.VINDICATOR, ::ProtocolVindicator)
        spawner(EntityType.RAVAGER, ::ProtocolRavager)
        spawner(EntityType.WITCH, ::ProtocolWitch)
        spawner(EntityType.SKELETON, ::ProtocolSkeleton)
        spawner(EntityType.STRAY, ::ProtocolStray)
        spawner(EntityType.WITHER_SKELETON, ::ProtocolWitherSkeleton)
        spawner(EntityType.CAVE_SPIDER, ::ProtocolCaveSpider)
        spawner(EntityType.SPIDER, ::ProtocolSpider)
        spawner(EntityType.DROWNED, ::ProtocolDrowned)
        spawner(EntityType.HUSK, ::ProtocolHusk)
        spawner(EntityType.ZOMBIE, ::ProtocolZombie)
        spawner(EntityType.ZOMBIE_VILLAGER, ::ProtocolZombieVillager)
        spawner(EntityType.ZOMBIFIED_PIGLIN, ::ProtocolZombifiedPiglin)
        spawner(EntityType.ALLAY, ::ProtocolAllay)
        spawner(EntityType.SLIME, ::ProtocolSlime)
        spawner(EntityType.COD, ::ProtocolCod)
        spawner(EntityType.PUFFERFISH, ::ProtocolPufferFish)
        spawner(EntityType.SALMON, ::ProtocolSalmon)
        spawner(EntityType.TADPOLE, ::ProtocolTadpole)
        spawner(EntityType.TROPICAL_FISH, ::ProtocolTropicalFish)
        spawner(EntityType.DOLPHIN, ::ProtocolDolphin)
        spawner(EntityType.GLOW_SQUID, ::ProtocolGlowSquid)
        spawner(EntityType.SQUID, ::ProtocolSquid)
        spawner(EntityType.EVOKER_FANGS, ::ProtocolEvokerFangs)
        spawner(EntityType.ARROW, ::ProtocolArrow)
        spawner(EntityType.SPECTRAL_ARROW, ::ProtocolSpectralArrow)
        spawner(EntityType.BOAT, ::ProtocolBoat)
        spawner(EntityType.CHEST_BOAT, ::ProtocolChestBoat)
        spawner(EntityType.DRAGON_FIREBALL, ::ProtocolDragonFireball)
        spawner(EntityType.FIREBALL, ::ProtocolFireball)
        spawner(EntityType.SMALL_FIREBALL, ::ProtocolSmallFireball)
        spawner(EntityType.GLOW_ITEM_FRAME, ::ProtocolGlowingItemFrame)
        spawner(EntityType.ARMOR_STAND, ::ProtocolArmorStand)
        spawner(EntityType.PLAYER, ::ProtocolPlayer)
        spawner(EntityType.MINECART, ::ProtocolMinecart)
        spawner(EntityType.MINECART_CHEST, ::ProtocolMinecartChest)
        spawner(EntityType.MINECART_COMMAND, ::ProtocolMinecartCommandBlock)
        spawner(EntityType.MINECART_FURNACE, ::ProtocolMinecartFurnace)
        spawner(EntityType.MINECART_HOPPER, ::ProtocolMinecartHopper)
        spawner(EntityType.MINECART_MOB_SPAWNER, ::ProtocolMinecartSpawner)
        spawner(EntityType.MINECART_TNT, ::ProtocolMinecartTNT)
        spawner(EntityType.AREA_EFFECT_CLOUD, ::ProtocolAreaEffectCloud)
        spawner(EntityType.ENDER_CRYSTAL, ::ProtocolEndCrystal)
        spawner(EntityType.ENDER_SIGNAL, ::ProtocolEyeOfEnder)
        spawner(EntityType.FALLING_BLOCK, ::ProtocolFallingBlock)
        spawner(EntityType.FIREWORK, ::ProtocolFireworkRocketEntity)
        spawner(EntityType.FISHING_HOOK, ::ProtocolFishingHook)
        spawner(EntityType.DROPPED_ITEM, ::ProtocolItemDrop)
        spawner(EntityType.LLAMA, ::ProtocolLlamaSpit)
        spawner(EntityType.PAINTING, ::ProtocolPainting)
        spawner(EntityType.PRIMED_TNT, ::ProtocolPrimedTnt)
        spawner(EntityType.WITHER_SKULL, ::ProtocolWitherSkull)
        spawner(EntityType.SNOWBALL, ::ProtocolSnowball)
        spawner(EntityType.EGG, ::ProtocolThrownEgg)
        spawner(EntityType.ENDER_PEARL, ::ProtocolThrownEnderPearl)
        spawner(EntityType.THROWN_EXP_BOTTLE, ::ProtocolThrownExperienceBottle)
        spawner(EntityType.SPLASH_POTION, ::ProtocolThrownPotion)
        spawner(EntityType.TRIDENT, ::ProtocolThrownTrident)
        spawner(EntityType.THROWN_EXP_BOTTLE, ::ProtocolThrownExperienceBottle)
        spawner(EntityType.SPLASH_POTION, ::ProtocolThrownPotion)
        spawner(EntityType.TRIDENT, ::ProtocolThrownTrident)
        spawner(EntityType.SNIFFER, ::ProtocolSniffer)
        spawner(EntityType.CAMEL, ::ProtocolCamel)

        spawner(EntityType.ITEM_FRAME, ::ProtocolItemFrame)
        spawner(EntityType.GLOW_ITEM_FRAME, ::ProtocolGlowingItemFrame)

        spawner(EntityType.ITEM_DISPLAY, ::ProtocolItemDisplay)
        spawner(EntityType.BLOCK_DISPLAY, ::ProtocolBlockDisplay)
        spawner(EntityType.TEXT_DISPLAY, ::ProtocolTextDisplay)
        spawner(EntityType.INTERACTION, ::ProtocolInteraction)
    }

    fun getType(clazz: Class<out ProtocolEntity>) =
        Class2Type[clazz] ?: throw IllegalArgumentException("Not registered entity clazz: $clazz")

    operator fun get(entityType: EntityType) =
        Type2Spawner[entityType] ?: throw IllegalArgumentException("Not registered entity spawner: $entityType")

    operator fun get(clazz: Class<out ProtocolEntity>) =
        Class2Type[clazz] ?: throw IllegalArgumentException("Not registered entity spawner: $clazz")

    inline fun <reified P : ProtocolEntity> spawner(type: EntityType, noinline spawner: (Location) -> P) {
        val clazz = P::class.java

        Type2Spawner[type] = spawner
        Class2Type[clazz] = type
        Class2Spawner[clazz] = spawner
    }

}

inline fun <reified E : ProtocolEntity> spawn(
    type: EntityType,
    location: Location,
    register: Boolean = true,
    block: E.() -> Unit = {}
) = ProtocolEntitySpawner[type](location).cast<E>().apply(block).apply { if (register) register() }

inline fun <reified E : ProtocolEntity> spawn(
    location: Location,
    register: Boolean = true,
    block: E.() -> Unit = {}
) = spawn(ProtocolEntitySpawner.getType(E::class.java), location, register, block)


