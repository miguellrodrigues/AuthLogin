package com.curiositty.listener

import com.curiositty.GAuthLogin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerEvents : Listener {

    companion object {
        private val iconManager = GAuthLogin.iconManager
    }

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent) {
        event.maxPlayers = event.numPlayers + 1
        event.setServerIcon(Bukkit.loadServerIcon(iconManager.getIcon(event.address.hostAddress)))
    }
}