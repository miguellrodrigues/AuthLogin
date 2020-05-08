package com.curiositty.listener

import com.curiositty.AuthLogin
import com.curiositty.events.PirateJoinEvent
import com.curiositty.manager.IconManager
import com.curiositty.manager.LoginManager
import com.curiositty.mysql.data.LoginData
import com.curiositty.utils.Strings
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerEvents : Listener {


    @EventHandler
    fun onPiratePlayerJoin(event: PirateJoinEvent) {
        val player = event.player

        LoginManager.lockPlayer(player)

        Bukkit.getServer().pluginManager.callEvent(PlayerQuitEvent(player, ""))
        Bukkit.getServer().pluginManager.callEvent(PlayerJoinEvent(player, ""))
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.joinMessage = null

        val player = event.player

        LoginData.createData(player.uniqueId)

        if (LoginManager.isPlayerLocked(player)) {
            if (!LoginData.registered(player.uniqueId))
                player.sendMessage("${Strings.AUTH} §fCaso queira adicionar seu código pelo QR Code, baste deslogar e adicionar pelo motd, ou digite §f[§ecode§f]")

            player.sendMessage("${Strings.AUTH} §fInforme o código do seu Autenticador!")

            return
        }

        IconManager.putPlayerName(player.name, player.address.hostName)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player

        if (LoginManager.isPlayerLocked(player)) {
            if (LoginManager.getSecret(player).isEmpty()) {
                LoginManager.putSecret(player, LoginData.getString(player.uniqueId, "code"))
            }
        }
    }

    @EventHandler
    fun onAsyncPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val message = event.message

        if (LoginManager.isPlayerLocked(player)) {
            event.isCancelled = true
            val secret = LoginData.getString(player.uniqueId, "code")

            if (event.message == "code") {
                player.sendMessage("${Strings.AUTH} §fSua key: §e$secret")
                return
            }

            val key: Int
            try {
                key = message.toInt()
            } catch (e: NumberFormatException) {
                player.sendMessage("${Strings.AUTH} §fdigite apenas números!")
                return
            }

            if (LoginManager.authenticatePlayer(player, key)) {
                object : BukkitRunnable() {
                    override fun run() {
                        player.sendMessage("${Strings.PREFIX} §fAutenticado com sucesso!")
                        LoginManager.unlockPlayer(player)
                    }
                }.runTaskLaterAsynchronously(AuthLogin.INSTANCE, 10L)
            } else {
                player.sendMessage("${Strings.AUTH} §fFalha ao se autenticar, tente novamente")
            }

            return
        }
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player

        if (LoginManager.isPlayerLocked(player)) {
            event.isCancelled = true
            player.sendMessage("${Strings.AUTH} §fCaso tenha esquecido sua key, digite §f[§ecode§f] ou adicione pelo QR Code no motd")
        }
    }
}