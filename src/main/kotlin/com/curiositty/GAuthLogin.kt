package com.curiositty

import com.curiositty.listener.PlayerEvents
import com.curiositty.mysql.MySqlConnector
import com.curiositty.login.Login
import com.curiositty.utils.Values
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class GAuthLogin : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: GAuthLogin
        lateinit var mysqlConnector: MySqlConnector
    }

    override fun onLoad() {
        INSTANCE = this

        dataFolder.mkdir()

        config.options().copyDefaults()
        config.options().copyHeader()

        saveDefaultConfig()

        Values.MYSQL_HOST = config.getString("Mysql.host")
        Values.MYSQL_USER = config.getString("Mysql.user")
        Values.MYSQL_PASSWORD = config.getString("Mysql.password")
        Values.MYSQL_SCHEMA = config.getString("Mysql.database")
    }

    override fun onEnable() {
        mysqlConnector = MySqlConnector()
        mysqlConnector.remoteConnection()

        server.pluginManager.registerEvents(PlayerEvents(), this)

        Login().enable()
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)

        saveConfig()
    }
}