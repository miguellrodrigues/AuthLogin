package com.curiositty.mysql

import com.curiositty.AuthLogin
import com.curiositty.utils.Values
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource

class MySqlConnector {

    private val instance = AuthLogin.INSTANCE

    companion object {
        lateinit var connection: Connection
    }

    private fun setConnection(con: Connection) {
        connection = con
    }

    fun remoteConnection() {
        println("Conectando ao servidor mysql remoto...")

        try {
            Class.forName("com.mysql.jdbc.Driver")

            val dataSource = MysqlDataSource()
            dataSource.serverName = Values.MYSQL_HOST
            dataSource.port = Values.MYSQL_PORT
            dataSource.databaseName = Values.MYSQL_SCHEMA
            dataSource.user = Values.MYSQL_USER
            dataSource.setPassword(Values.MYSQL_PASSWORD)
            dataSource.serverTimezone = "UTC"

            setConnection(dataSource.connection)
            println("Conectado com sucesso!")
        } catch (e: Exception) {
            println("Erro ao conectar ao servidor mysql remoto... ${e.localizedMessage}")
            liteConnection()
        }
    }

    private fun liteConnection() {
        val file = File(instance.dataFolder, "AuthLogin.db")
        if(!file.exists())
            file.createNewFile()

        val url = "jdbc:sqlite:$file"
        try {
            Class.forName("org.sqlite.JDBC")
            setConnection(DriverManager.getConnection(url))

            println("Conexao SQLite inicializada")
        } catch (e: Exception) {
            throw Error(e.message)
        }
    }
}