package com.curiositty.manager

import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

object IconManager {

    private val headIcon: HashMap<String, String> = HashMap()

    private fun getPlayerName(hostAddress: String): String {
        if (headIcon.containsKey(hostAddress))
            return headIcon[hostAddress]!!

        return ""
    }

    fun putPlayerName(name: String, hostAddress: String) {
        if (getPlayerName(hostAddress).isEmpty())
            headIcon[hostAddress] = name
    }

    fun getIcon(hostAddress: String): BufferedImage {
        var url: URL = when {
            LoginManager.getSecret(hostAddress).isNotEmpty() -> {
                URL(
                    "https://www.google.com/chart?chs=64x64&chld=M|0&cht=qr&chl=otpauth://totp/AuthLogin?secret=${LoginManager.getSecret(
                        hostAddress
                    )}&issuer=AuthLogin"
                )
            }
            getPlayerName(hostAddress).isNotEmpty() -> {
                URL("https://minotar.net/helm/${getPlayerName(hostAddress)}/64")
            }
            else -> {
                URL("https://minotar.net/helm/Notch/64")
            }
        }

        return ImageIO.read(url)
    }
}