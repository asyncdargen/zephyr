package dev.zephyr.protocol.scoreboard

import dev.zephyr.protocol.ProtocolObject
import dev.zephyr.util.kotlin.KotlinOpens
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.entity.Player

@KotlinOpens
class ProtocolScoreboard(val name: String = RandomStringUtils.randomAlphanumeric(16)) : ProtocolObject() {

    val objective = ProtocolScoreboardObjective(name)

    var title by objective::titleComponent
    val lines: MutableMap<Int, String> = hashMapOf()

    fun getLine(index: Int) = lines[index]

    fun setLine(index: Int, content: String) {
        lines[index] = content

        objective.getOrCreateScore(index.scoreName).score = index
        objective.getOrCreateTeam(index.scoreName, index.scoreName).prefix = content
    }

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