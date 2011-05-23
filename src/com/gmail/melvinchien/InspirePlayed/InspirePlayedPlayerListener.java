package com.gmail.melvinchien.InspirePlayed;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
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
	
	// When player joins, check if player exists, capture current time,
	// load old playtime, TIME IN HOURS
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		int login = (int)(System.currentTimeMillis() / 1000L);
		boolean newPlayer = InspirePlayedDataFile.containsKey(player, InspirePlayed.playtimes);
		if (newPlayer == false) {
			InspirePlayedDataFile.write(player, 0.0, login, InspirePlayed.playtimes);
		}
	}

	// Check for /played command
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("played")) {
			return true;
		}
		return false;
	}
	
}