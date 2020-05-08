package com.curiositty.mysql

import com.curiositty.AuthLogin
import com.mysql.cj.jdbc.MysqlDataSource
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

object MySqlConnector {

    private val instance = AuthLogin.INSTANCE

    lateinit var connection: Connection

    fun remoteConnection() {
        println("Conectando ao servidor mysql remoto...")

        try {
            Class.forName("com.mysql.cj.jdbc.Driver")

            val dataSource = MysqlDataSource()
            dataSource.serverName = "localhost"
            dataSource.port = 3306
            dataSource.databaseName = "dev"
            dataSource.user = "root"
            dataSource.password = "root"
            dataSource.serverTimezone = "UTC"

            connection = dataSource.connection
            println("Conectado com sucesso!")
        } catch (e: Exception) {
            println("Erro ao conectar ao servidor mysql remoto... ${e.localizedMessage}")
            liteConnection()
        }
    }

    private fun liteConnection() {
        val file = File(instance.dataFolder, "AuthLogin.db")
        if (!file.exists())
            file.createNewFile()

        val url = "jdbc:sqlite:$file"
        try {
            Class.forName("org.sqlite.JDBC")
            connection = DriverManager.getConnection(url)

            println("Conexao SQLite inicializada")
        } catch (e: Exception) {
            throw Error(e.message)
        }
    }
}