package com.curiositty.mysql.data

import com.curiositty.GAuthLogin
import com.warrenstrange.googleauth.GoogleAuthenticator
import java.util.*
import kotlin.collections.HashMap

class LoginData {

    companion object {
        private val mySqlManager = GAuthLogin.mySqlManager
        private val storedDataMap: HashMap<UUID, Data> = HashMap()
        private val gameDataMap: HashMap<UUID, Data> = HashMap()
    }

    fun loadAllData() {
        val allPlayers = mySqlManager.getAllPlayers()

        allPlayers.forEach { uuid ->
            val data = Data()

            data.uuid = uuid
            data.code = mySqlManager.getString(uuid, "code")

            storedDataMap[uuid] = data
        }
    }

    private fun loadData(uuid: UUID) {
        if(gameDataMap.containsKey(uuid))
            return

        val data = Data()

        data.code = storedDataMap[uuid]!!.code
        data.uuid = uuid

        gameDataMap[uuid] = data
    }

    fun saveData() {
        gameDataMap.keys.forEach { uuid ->
            val gameData = gameDataMap[uuid]
            gameData!!.save()
        }
    }

    fun createData(uuid: UUID) {
        if (storedDataMap.containsKey(uuid) || gameDataMap.containsKey(uuid)) {
            loadData(uuid)
            return
        }

        val data = Data()
        val key = GoogleAuthenticator().createCredentials().key

        data.uuid = uuid
        data.code = key

        println("Created data: $uuid")

        gameDataMap[uuid] = data
    }

    fun getString(uuid: UUID, column: String): String {
        if (!gameDataMap.containsKey(uuid))
            return ""

        lateinit var str: String

        val data = gameDataMap[uuid]!!

        when (column) {
            "code" -> {
                str = data.code
            }

            else -> {
            }

        }

        return str
    }

    fun setString(uuid: UUID, column: String, value: String) {
        if (!gameDataMap.containsKey(uuid))
            return

        val data = gameDataMap[uuid]!!

        when (column) {
            "code" -> {
                data.code = value
            }

            else -> {
            }
        }
    }

    class Data {
        lateinit var uuid: UUID
        lateinit var code: String

        fun save() {
            if (mySqlManager.playerExist(uuid))
                return

            mySqlManager.createPlayer(uuid)
            mySqlManager.setString(uuid, "code", code)
        }
    }
}