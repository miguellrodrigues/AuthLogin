package me.dark.utils.others

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class CheckPremium(nick: String?) {

    var nome: String? = null
    var mojangAPI = "https://api.mojang.com/profiles/minecraft"
    val result: Boolean
        get() {
            var l: String? = null
            try {
                l = enviarPost("[ \"$nome\",\"nonExistingPlayer\"  ]", URL(mojangAPI))
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return if (l == null) {
                true
            } else l.contains("name") && !l.contains("demo")
        }

    @Throws(Exception::class)
    private fun enviarPost(payload: String, url: URL): String? {
        val con = url.openConnection() as HttpsURLConnection
        con.readTimeout = 15000
        con.connectTimeout = 15000
        con.requestMethod = "POST"
        con.setRequestProperty("Content-Type", "application/json")
        con.doInput = true
        con.doOutput = true
        val out = con.outputStream
        out.write(payload.toByteArray(charset("UTF-8")))
        out.close()
        val `in` = BufferedReader(InputStreamReader(con.inputStream))
        var output: String? = ""
        var line: String?
        while (`in`.readLine().also { line = it } != null) output += line
        `in`.close()
        return output
    }

    init {
        nome = nick
    }
}