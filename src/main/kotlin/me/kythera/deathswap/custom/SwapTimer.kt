package me.kythera.deathswap.custom

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class SwapTimer(
    private val plugin: JavaPlugin,
    private val configData: ConfigData<*>,
    private val onSwap: () -> Unit
) {
    private var task: BukkitTask? = null
    private var ticksRemaining: Long = 0L

    fun start() {
        stop()
        resetTimer()

        task = object : BukkitRunnable() {
            override fun run() {
                if (ticksRemaining <= 0L) {
                    onSwap()
                    resetTimer()
                    return
                }

                val secondsLeft = ticksRemaining / 20L
                if (secondsLeft in 1..10) {
                    plugin.server.showTitle(
                        Title.title(
                            Component.text("${secondsLeft}s", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
                            Component.text("YOU WILL BE TELEPORTED!", NamedTextColor.GRAY)
                        )
                    )
                }

                ticksRemaining -= 20L
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }

    fun stop() {
        task?.cancel()
        task = null
    }

    private fun resetTimer() {
        ticksRemaining = configData.getNextSwapTicks()
    }
}
