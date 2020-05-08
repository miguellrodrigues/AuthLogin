package com.curiositty.manager

import com.curiositty.mysql.data.LoginData
import com.warrenstrange.googleauth.GoogleAuthenticator
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object LoginManager {

    private val lockedPlayers: MutableList<UUID> = ArrayList()
    private val piratePlayers: MutableList<UUID> = ArrayList()

    private val qrCode: HashMap<String, String> = HashMap()

    private val gAuth = GoogleAuthenticator()

    fun lockPlayer(player: Player) {
        if (!lockedPlayers.contains(player.uniqueId))
            lockedPlayers.add(player.uniqueId)

        qrCode[player.address.hostName] = LoginData.getString(player.uniqueId, "code")
    }

    fun lockPlayer(uuid: UUID, hostName: String) {
        if (!lockedPlayers.contains(uuid))
            lockedPlayers.add(uuid)

        qrCode[hostName] = LoginData.getString(uuid, "code")
    }

    fun unlockPlayer(player: Player) {
        if (lockedPlayers.contains(player.uniqueId))
            lockedPlayers.remove(player.uniqueId)

        if (getSecret(player).isNotEmpty())
            qrCode.remove(player.address.hostName)

        piratePlayers.add(player.uniqueId)
    }

    fun authenticatePlayer(player: Player, key: Int): Boolean {
        val secret = LoginData.getString(player.uniqueId, "code")
        return gAuth.authorize(secret, key)
    }

    fun isPlayerLocked(player: Player): Boolean {
        return lockedPlayers.contains(player.uniqueId)
    }

    fun getSecret(host: String): String {
        if (qrCode.containsKey(host))
            return qrCode[host]!!

        return ""
    }

    fun getSecret(player: Player): String {
        if (qrCode.containsKey(player.address.hostName))
            return qrCode[player.address.hostName]!!

        return ""
    }

    fun putSecret(player: Player, secret: String) {
        if (!qrCode.containsKey(player.address.hostName))
            qrCode[player.address.hostName] = secret
    }
}