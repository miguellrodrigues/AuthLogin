package com.curiositty

import com.curiositty.listener.PlayerEvents
import com.curiositty.listener.ServerEvents
import com.curiositty.mysql.MySqlConnector
import com.curiositty.login.Login
import com.curiositty.manager.LoginManager
import com.curiositty.manager.MySqlManager
import com.curiositty.mysql.data.LoginData
import com.curiositty.utils.Values
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class GAuthLogin : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: GAuthLogin
        lateinit var mysqlConnector: MySqlConnector
        lateinit var mySqlManager: MySqlManager
        lateinit var loginData: LoginData
        lateinit var loginManager: LoginManager
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
    }

    override fun onEnable() {
        mysqlConnector = MySqlConnector()
        mysqlConnector.remoteConnection()

        mySqlManager = MySqlManager()

        loginData = LoginData()
        loginData.loadAllData()

        loginManager = LoginManager()

        server.pluginManager.registerEvents(PlayerEvents(), this)
        server.pluginManager.registerEvents(ServerEvents(), this)

        Login().enable()
    }

    override fun onDisable() {
        HandlerList.unregisterAll(this)

        saveConfig()

        loginData.saveData()
    }
}