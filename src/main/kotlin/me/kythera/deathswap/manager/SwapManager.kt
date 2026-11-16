package me.kythera.deathswap.manager

import me.kythera.deathswap.custom.SwapTimer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.logger.slf4j.ComponentLogger
import net.kyori.adventure.title.Title
import org.bukkit.Server
import java.time.Duration

class SwapManager(
    private val swapTimer: SwapTimer,
    private val componentLogger: ComponentLogger,
    private val server: Server
) {
    private var started = false

    fun start() {
        if (!started) {
            started = true
            swapTimer.start()
            componentLogger.debug("The Swap Timer has been started.")
            server.onlinePlayers.forEach { player ->
                player.showTitle(
                    Title.title(
                        Component.text("EVENT STARTED: DEATHSWAP", NamedTextColor.YELLOW),
                        Component.text("YOU WILL BE SWAPPED RANDOMLY!", NamedTextColor.GRAY),
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )
            }
        }
    }

    fun stop() {
        if (started) {
            started = false
            swapTimer.stop()
            componentLogger.debug("The Swap Timer has been stopped.")
            server.onlinePlayers.forEach { player ->
                player.showTitle(
                    Title.title(
                        Component.text("EVENT STOPPED", NamedTextColor.RED),
                        Component.text("DEATHSWAP HAS ENDED.", NamedTextColor.GRAY),
                        Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
                    )
                )
            }
        }
    }

    fun isStarted(): Boolean = started
}
