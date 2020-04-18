package com.curiositty.listener

import com.curiositty.events.PirateJoinEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class PlayerEvents : Listener {

    @EventHandler
    fun onPiratePlayerJoin(event: PirateJoinEvent) {
        val player = event.player
        player.sendMessage(event.joinMessage)


    }

    @EventHandler
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message


    }
}