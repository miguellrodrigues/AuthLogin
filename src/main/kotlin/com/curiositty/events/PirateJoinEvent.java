package com.curiositty.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PirateJoinEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private String joinMessage;

    public PirateJoinEvent(Player playerJoined, String joinMessage) {
        super(playerJoined);
        this.joinMessage = joinMessage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public String getJoinMessage() {
        return this.joinMessage;
    }

    public void setJoinMessage(String joinMessage) {
        this.joinMessage = joinMessage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
