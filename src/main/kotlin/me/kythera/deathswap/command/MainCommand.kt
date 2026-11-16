package me.kythera.deathswap.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import me.kythera.deathswap.manager.SwapManager
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

object MainCommand {
    fun getCommand(swapManager: SwapManager): LiteralCommandNode<CommandSourceStack> {
        return Commands.literal("deathswap")
            .requires { source ->
                source.sender.hasPermission("deathswap.start") ||
                        source.sender.hasPermission("deathswap.stop")
            }
            .then(
                Commands.literal("start")
                    .requires { source -> source.sender.hasPermission("deathswap.start") }
                    .executes { ctx ->
                        if (swapManager.isStarted()) {
                            ctx.source.sender.sendMessage(
                                Component.text("DeathSwap is already running!", NamedTextColor.RED)
                            )
                        } else {
                            swapManager.start()
                            ctx.source.sender.sendMessage(
                                Component.text("Started DeathSwap.", NamedTextColor.GREEN)
                            )
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
            .then(
                Commands.literal("stop")
                    .requires { source -> source.sender.hasPermission("deathswap.stop") }
                    .executes { ctx ->
                        if (!swapManager.isStarted()) {
                            ctx.source.sender.sendMessage(
                                Component.text("DeathSwap is not currently running.", NamedTextColor.RED)
                            )
                        } else {
                            swapManager.stop()
                            ctx.source.sender.sendMessage(
                                Component.text("Stopped DeathSwap.", NamedTextColor.YELLOW)
                            )
                        }
                        Command.SINGLE_SUCCESS
                    }
            )
            .build()
    }
}
