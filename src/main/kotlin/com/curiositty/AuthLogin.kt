package com.curiositty

import com.curiositty.listener.PlayerEvents
import com.curiositty.listener.ServerEvents
import com.curiositty.login.Login
import com.curiositty.mysql.MySqlConnector
import com.curiositty.mysql.data.LoginData
import com.curiositty.utils.Values
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class AuthLogin : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: AuthLogin
    }

    override fun onLoad() {
        INSTANCE = this

        dataFolder.mkdir()

        config.options().copyDefaults()
        config.options().copyHeader()

        saveDefaultConfig()

        Values.MYSQL_HOST = config.getString("Mysql.host")
        Values.MYSQL_PORT = config.getInt("Mysql.port")
        Values.MYSQL_USER = config.getString("Mysql.user")
        Values.MYSQL_PASSWORD = config.getString("Mysql.password")
        Values.MYSQL_SCHEMA = config.getString("Mysql.database")

        MySqlConnector.remoteConnection()

        LoginData.loadAllData()
    }

    override fun onEnable() {
        server.pluginManager.registerEvents(PlayerEvents(), this)
        server.pluginManager.registerEvents(ServerEvents(), this)

        Login().enable()
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)

        saveConfig()

        LoginData.saveData()
    }
}