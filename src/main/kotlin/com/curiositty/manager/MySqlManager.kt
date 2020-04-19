package com.curiositty.manager

import com.curiositty.mysql.MySqlConnector
import java.util.*

class MySqlManager {

    private val table = "gauthlogin_players"
    private val connection = MySqlConnector.connection

    init {
        init()
    }

    private fun init() {
        try {

            val statement = connection.prepareStatement(
                "CREATE TABLE IF NOT EXISTS $table(id INT AUTO_INCREMENT PRIMARY KEY, uuid VARCHAR(255), code VARCHAR(255))"
            )

            statement.execute()
            statement.close()

        } catch (e: Exception) {
            throw Error(e.message)
        }
    }

    fun playerExist(uuid: UUID): Boolean {
        try {

            val statement = connection.prepareStatement(
                "SELECT * FROM $table WHERE uuid=?"
            )

            statement.setString(1, uuid.toString())

            val resultSet = statement.executeQuery()
            if (resultSet.next())
                return true

            statement.close()

        } catch (e: Exception) {
            throw Error(e.message)
        }

        return false
    }

    fun createPlayer(uuid: UUID) {
        if(playerExist(uuid))
            return

        try{
            val statement = connection.prepareStatement(
                "INSERT INTO $table(uuid, code) VALUES ('$uuid', '')")

            statement.execute()
            statement.close()

        }catch (e: Exception) {
            throw Error(e.message)
        }
    }

    fun setString(uuid: UUID, column: String, value: String) {
        try{
            val statement = connection.prepareStatement(
                "UPDATE $table SET $column = '$value' WHERE uuid='${uuid}'")

            statement.executeUpdate()
            statement.close()

        }catch (e: Exception) {
            throw Error(e.message)
        }
    }

    fun getString(uuid: UUID, column: String) : String {
        lateinit var str: String

        try{
            val statement = connection.prepareStatement(
                "SELECT * FROM $table WHERE uuid=?")

            statement.setString(1, uuid.toString())

            val resultSet = statement.executeQuery()
            if (resultSet.next()) {
                str = resultSet.getString(column)
            }

            statement.close()
            resultSet.close()

        }catch (e: Exception) {
            throw Error(e.message)
        }

        return str
    }

    fun getAllPlayers(): MutableList<UUID> {
        val list: MutableList<UUID> = ArrayList()

        try {
            val statement = connection.prepareStatement(
                "SELECT * FROM $table"
            )

            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                list.add(UUID.fromString(resultSet.getString("uuid")))
            }

        } catch (e: Exception) {
            throw Error(e.localizedMessage)
        }

        return list
    }
}