package com.curiositty.listener

import com.curiositty.GAuthLogin
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.server.ServerListPingEvent
import java.net.URL
import javax.imageio.ImageIO

class ServerEvents : Listener {

    companion object {
        private val loginManager = GAuthLogin.loginManager
    }

    @EventHandler
    fun onServerListPing(event: ServerListPingEvent) {
        event.maxPlayers = event.numPlayers + 1

        lateinit var url: URL
        val hostAddress = event.address.hostAddress

        url = if(!loginManager.getSecret(hostAddress).isEmpty()) {
            URL("https://www.google.com/chart?chs=64x64&chld=M|0&cht=qr&chl=otpauth://totp/GAuthLogin?secret=${loginManager.getSecret(hostAddress)}&issuer=GAuthLogin")
        /*} else if (Values.icon!!.containsKey(hostAddress)) {
            URL("https://minotar.net/helm/${Values.icon!![hostAddress]}/64")*/
        } else {
            URL("https://minotar.net/helm/Notch/64")
        }

        val icon = ImageIO.read(url)
        event.setServerIcon(Bukkit.loadServerIcon(icon))
    }
}