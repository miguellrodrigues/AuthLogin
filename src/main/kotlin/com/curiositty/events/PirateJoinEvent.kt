package com.curiositty.events

import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class PirateJoinEvent(playerJoined: Player?, message: String) : PlayerEvent(playerJoined) {

    val joinMessage: String = message

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        val handlerList = HandlerList()
    }
}
