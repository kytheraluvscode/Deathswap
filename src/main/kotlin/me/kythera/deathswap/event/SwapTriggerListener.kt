package me.kythera.deathswap.event

import me.kythera.deathswap.manager.SwapManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.title.Title
import net.kyori.adventure.title.Title.title
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.Server
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.time.Duration

class SwapTriggerListener(
    private val server: Server,
    private val swapManager: SwapManager
) : Listener {

    @EventHandler
    fun onSwap(event: SwapTriggerEvent) {
        if (!swapManager.isStarted()) return

        val participants = server.onlinePlayers
            .filter { it.gameMode != GameMode.SPECTATOR }
            .shuffled()

        if (participants.size < 2) return

        val savedLocations: List<Location> = participants.map { it.location.clone() }
        val swapTitle = title(
            Component.text("SWAPPED!", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD),
            Component.text("YOU'VE BEEN TELEPORTED", NamedTextColor.GRAY),
            Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
        )

        for (i in participants.indices) {
            val player = participants[i]
            val targetIndex = (i + 1) % participants.size
            val targetLocation = savedLocations[targetIndex]
            val targetPlayer = participants[targetIndex]
            val wasOnFire = targetPlayer.fireTicks > 0

            player.teleport(targetLocation)
            player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)

            player.world.spawnParticle(
                Particle.TOTEM_OF_UNDYING,
                player.location.add(0.0, 1.0, 0.0),
                50,
                0.5, 0.8, 0.5,
                0.2
            )

            if (wasOnFire || player.fireTicks > 0) {
                player.addPotionEffect(PotionEffect(PotionEffectType.FIRE_RESISTANCE, 1200, 0))
            }

            player.showTitle(swapTitle)
        }
    }
}
