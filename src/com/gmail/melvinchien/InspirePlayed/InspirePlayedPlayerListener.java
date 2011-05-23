package com.gmail.melvinchien.InspirePlayed;

/*
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
*/
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
//import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;


/**
 * Handle events for all Player related events
 * @author VeiN
 */

public class InspirePlayedPlayerListener extends PlayerListener {
    private final InspirePlayed plugin;

	public InspirePlayedPlayerListener(InspirePlayed instance) {
		plugin = instance;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		plugin.login(event.getPlayer().getName().toLowerCase());
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.logout(event.getPlayer().getName().toLowerCase());
	}

	

	
}