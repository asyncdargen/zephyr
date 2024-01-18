package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.util.component.toComponent
import dev.zephyr.util.kotlin.KotlinOpens
import net.kyori.adventure.text.Component
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolScoreboard(val name: String = RandomStringUtils.randomAlphanumeric(16)) : ProtocolObject() {

    val objective = ProtocolScoreboardObjective(name)

    var title by objective::title
    val lines: MutableMap<Int, Component> = hashMapOf()

    fun getLine(index: Int) = lines[index]

    fun setLine(index: Int, content: Component) {
        lines[index] = content

        objective.getOrCreateScore(index.scoreName).score = index
        objective.getOrCreateTeam(index.scoreName, index.scoreName).prefix = content
    }

    fun setLine(index: Int, content: String) = setLine(index, content.toComponent())

    fun removeLine(index: Int) {
        lines.remove(index)

        objective.removeScore(index.scoreName)
        objective.removeTeam(index.scoreName)
    }

    override fun spawn(players: Collection<Player>) {
        super.spawn(players)
        objective.spawn(players)
    }

    override fun destroy(players: Collection<Player>) {
        super.destroy(players)
        objective.destroy(players)
    }

    override fun sendSpawnPackets(players: Collection<Player>) {

    }

    override fun sendDestroyPackets(players: Collection<Player>) {

    }

    val Int.scoreName
        get() = "§${toChar()}"

}