package com.curiositty.listener

import com.curiositty.manager.IconManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent

class ServerEvents : Listener {

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent) {
        event.setServerIcon(Bukkit.loadServerIcon(IconManager.getIcon(event.address.hostAddress)))
    }
}