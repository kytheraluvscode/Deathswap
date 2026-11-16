package me.kythera.deathswap.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class SwapTriggerEvent : Event() {
    override fun getHandlers(): HandlerList = HANDLER_LIST

    companion object {
        private val HANDLER_LIST = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLER_LIST
    }
}