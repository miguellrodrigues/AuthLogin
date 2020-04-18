package com.curiositty.login

import com.curiositty.GAuthLogin
import com.curiositty.events.PirateJoinEvent
import com.curiositty.login.utils.GameProfileBuilder
import com.curiositty.reflection.Reflection
import com.curiositty.reflection.TinyProtocol
import com.mojang.authlib.GameProfile
import io.netty.channel.Channel
import me.dark.utils.others.CheckPremium
import net.minecraft.server.v1_9_R2.*
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.net.SocketAddress
import java.util.*

class Login {

    private val gameProfile = Reflection.getField("{nms}.PacketLoginInStart", GameProfile::class.java, 0)

    fun enable() {

        object : TinyProtocol(GAuthLogin.INSTANCE) {
            override fun onPacketInAsync(sender: Player?, channel: Channel?, packet: Any?): Any? {

                if (packet is PacketLoginInStart) {
                    if (gameProfile.hasField(packet)) {
                        var profile = gameProfile.get(packet)

                        if (!(CheckPremium(profile.name).result)) {
                            BypassLogin(profile, MinecraftServer.getServer(), networkList(channel!!.remoteAddress())!!)
                            return null
                        } else {
                            profile = GameProfile(GameProfileBuilder.UUIDFetcher.getUUID(profile.name), profile.name)

                            if (!loginCheck(profile.name, profile.id)) {
                                disconnect("Ocorreu um erro, por favor, tente novamente.", networkList(channel!!.localAddress())!!)
                            }
                        }
                    }
                }

                return super.onPacketInAsync(sender, channel, packet)
            }
        }
    }

    private fun disconnect(msg: String, manager: NetworkManager) {
        try {
            val exception = ChatComponentText(msg)
            manager.handleDisconnection()
            manager.close(exception)
        } catch (e: Exception) {
            throw Error(e.message)
        }
    }

    private fun loginCheck(name: String, uuid: UUID): Boolean {
        return GameProfileBuilder.UUIDFetcher.getUUID(name) == uuid
    }

    private fun networkList(socketAddress: SocketAddress): NetworkManager? {
        var nm: NetworkManager? = null

        try {
            for (next in Reflection.ClassReflection.getField("h", MinecraftServer.getServer().am(), 0) as List<NetworkManager>) {
                if (next.socketAddress == socketAddress) {
                    nm = next
                    break
                }
            }
        } catch (e: Exception) {
            throw Error(e.message)
        }

        return nm
    }


    private class BypassLogin(profile: GameProfile, ms: MinecraftServer, nm: NetworkManager) : LoginListener(ms, nm) {

        init {
            /*try {
                Reflection.ClassReflection.setField("m", this, networkManager, 0)
                Reflection.ClassReflection.setField("i", profile, this, 1)
            } catch (e1: Exception) {
                System.err.println(e1.message)
            }*/
        }

        override fun a(packetlogininencryptionbegin: PacketLoginInEncryptionBegin?) {
        }

        override fun b() {
            c()
        }

        override fun c() {
            try {
                var profile = Reflection.ClassReflection.getField("i", this, 1) as GameProfile
                profile = a(profile)

                networkManager.sendPacket(PacketLoginOutSuccess(profile))

                val server = MinecraftServer.getServer()
                var player = EntityPlayer(MinecraftServer.getServer(), MinecraftServer.getServer().getWorldServer(0), profile, PlayerInteractManager(MinecraftServer.getServer().getWorldServer(0)))

                player = server.playerList.processLogin(player.profile, player)

                server.playerList.a(this.networkManager, player)

                Bukkit.getServer().pluginManager.callEvent(PirateJoinEvent(Bukkit.getPlayer(player.uniqueID), ""))

                var h = Reflection.ClassReflection.getField("h", this, 1) as Int
                h++
                if (h == 600) {
                    disconnect("Took too long to log in")
                }

            } catch (e: Exception) {
                System.err.println(e.localizedMessage)
            }
        }
    }
}