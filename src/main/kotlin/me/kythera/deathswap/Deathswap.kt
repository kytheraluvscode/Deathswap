package me.kythera.deathswap

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import me.kythera.deathswap.command.MainCommand
import me.kythera.deathswap.custom.ConfigData
import me.kythera.deathswap.custom.FixedConfigData
import me.kythera.deathswap.custom.RandomConfigData
import me.kythera.deathswap.custom.SwapTimer
import me.kythera.deathswap.event.SwapTriggerEvent
import me.kythera.deathswap.event.SwapTriggerListener
import me.kythera.deathswap.manager.SwapManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.plugin.java.JavaPlugin

class Deathswap : JavaPlugin() {

    lateinit var configData: ConfigData<*>
    lateinit var swapManager: SwapManager
    lateinit var swapTimer: SwapTimer

    override fun onEnable() {
        saveDefaultConfig()
        configData = loadConfigData()
        swapTimer = SwapTimer(this, configData) { SwapTriggerEvent().callEvent() }
        swapManager = SwapManager(swapTimer, componentLogger, server)

        server.pluginManager.registerEvents(SwapTriggerListener(server, swapManager), this)
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            event.registrar().register(MainCommand.getCommand(swapManager))
        }

        componentLogger.info(
            Component.text("Deathswap is enabled", NamedTextColor.GREEN)
        )
    }

    override fun onDisable() {
        if (::swapManager.isInitialized) {
            swapManager.stop()
        }
        componentLogger.info(
            Component.text("Deathswap is disabled", NamedTextColor.RED)
        )
    }

    fun loadConfigData(): ConfigData<*> {
        val modeString = config.getString("mode")?.uppercase() ?: "FIXED"

        return when (modeString) {
            "RANDOM" -> {
                val rawMin = config.getLong("random.min", 12000L)
                val rawMax = config.getLong("random.max", 18000L)

                val safeMin = minOf(rawMin, rawMax).coerceAtLeast(20L)
                val safeMax = maxOf(rawMin, rawMax).coerceIn(safeMin, Long.MAX_VALUE - 1)
                RandomConfigData(safeMin, safeMax)
            }

            else -> {
                val time = config.getLong("fixed.time", 12000L)
                FixedConfigData(time)
            }
        }
    }
}
