package com.curiositty

import com.curiositty.listener.PlayerEvents
import com.curiositty.mysql.MySqlConnector
import org.bukkit.plugin.java.JavaPlugin

class GAuthLogin : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: GAuthLogin
        lateinit var mysqlConnector: MySqlConnector
    }

    override fun onLoad() {

        INSTANCE = this
        mysqlConnector = MySqlConnector()

        mysqlConnector.liteConnection()
    }

    override fun onEnable() {

        server.pluginManager.registerEvents(PlayerEvents(), this)

    }

    override fun onDisable() {
    }
}