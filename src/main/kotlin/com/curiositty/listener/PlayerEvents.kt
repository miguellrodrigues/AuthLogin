package com.curiositty.listener

import com.curiositty.events.PirateJoinEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent

class PlayerEvents : Listener {

    @EventHandler
    fun onPiratePlayerJoin(event: PirateJoinEvent) {
        event.joinMessage = null

        val player = event.player
    }

    @EventHandler
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message


    }
}