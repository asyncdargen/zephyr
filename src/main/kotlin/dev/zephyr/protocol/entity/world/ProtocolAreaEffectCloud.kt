package dev.zephyr.protocol.entity.world

import com.comphenix.protocol.wrappers.WrappedParticle
import dev.zephyr.protocol.entity.ProtocolEntity
import dev.zephyr.protocol.entity.metadata.MetadataType
import dev.zephyr.util.kotlin.KotlinOpens
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.EntityType

@KotlinOpens
class ProtocolAreaEffectCloud(location: Location) : ProtocolEntity(EntityType.AREA_EFFECT_CLOUD, location) {

    var radius by metadata.item(8, MetadataType.Float, .5f)
    var color by metadata.item(9, MetadataType.Int, 0)
    var ignoreRadiusAndShowEffectAsSinglePoint by metadata.item(10, MetadataType.Boolean,false)
    var particle by metadata.item(11, MetadataType.Particle, WrappedParticle.create(Particle.CRIT, null))

}