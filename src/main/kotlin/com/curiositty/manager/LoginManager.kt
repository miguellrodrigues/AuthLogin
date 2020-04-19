package com.curiositty.manager

import com.curiositty.GAuthLogin
import com.warrenstrange.googleauth.GoogleAuthenticator
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class LoginManager {

    companion object {
        val lockedPlayers: MutableList<UUID> = ArrayList()
        val piratePlayers: MutableList<UUID> = ArrayList()

        val loginData = GAuthLogin.loginData

        val qrCode: HashMap<String, String> = HashMap()

        val gAuth = GoogleAuthenticator()
    }

    fun lockPlayer(player: Player) {
        if(!lockedPlayers.contains(player.uniqueId))
            lockedPlayers.add(player.uniqueId)

        qrCode[player.address.hostName] = loginData.getString(player.uniqueId, "code")
    }

    fun unlockPlayer(player: Player) {
        if(lockedPlayers.contains(player.uniqueId))
            lockedPlayers.remove(player.uniqueId)

        piratePlayers.add(player.uniqueId)
    }

    fun authenticatePlayer(player: Player, key: Int) : Boolean {
        val secret = loginData.getString(player.uniqueId, "code")
        return gAuth.authorize(secret, key)
    }

    fun isPlayerLocked(player: Player) : Boolean {
        return lockedPlayers.contains(player.uniqueId)
    }

    fun getSecret(host: String) : String {
        if(qrCode.containsKey(host))
            return qrCode[host]!!

        return ""
    }

    fun getSecret(player: Player) : String {
        if(qrCode.containsKey(player.address.hostName))
            return qrCode[player.address.hostName]!!

        return ""
    }

    fun putSecret(player: Player, secret: String) {
        if(!qrCode.containsKey(player.address.hostName))
            qrCode[player.address.hostName] = secret
    }

    fun getPiratePlayers() : MutableList<UUID> {
        return piratePlayers
    }
}