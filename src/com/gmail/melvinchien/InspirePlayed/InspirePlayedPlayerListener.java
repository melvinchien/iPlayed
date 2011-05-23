package com.gmail.melvinchien.InspirePlayed;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
    public HashMap<Player, ArrayList<Integer>> mapTimes;
    private ArrayList<Integer> arrayTimes = new ArrayList<Integer>();

	public InspirePlayedPlayerListener(InspirePlayed instance) {
		plugin = instance;
		mapTimes = InspirePlayed.mapTimes;
	}
	
	// When player joins, check if player exists, capture current time,
	// load old playtime, TIME IN MINUTEs
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		int login = (int)(System.currentTimeMillis() / 1000L);
		arrayTimes = new ArrayList<Integer>(2);
		arrayTimes.add(0);
		arrayTimes.add(0);
		if (!mapTimes.containsKey(player)) {
			mapTimes.put(player, arrayTimes);
		} else {
			arrayTimes = mapTimes.get(player);			
		}
		arrayTimes.set(1, login);
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		int logout = (int)(System.currentTimeMillis() / 1000L);
		arrayTimes = mapTimes.get(player);
		int login = arrayTimes.get(1);
		int currentPlaytime = (int)((logout - login) / 60);
		int oldPlaytime = arrayTimes.get(0);
		int newPlaytime = oldPlaytime + currentPlaytime;
		arrayTimes.set(0, newPlaytime);
		arrayTimes.set(1, 0);
	}

	// Check for /played command and display playtime to sender
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("/played")) {
			Player player = (Player) sender;
			player.sendMessage("You have played for " + getPlaytime(player) + " minutes.");
			return true;
		}
		return false;
	}
	
	// Get a player's playtime in minutes
	public int getPlaytime(Player player) {
		arrayTimes = mapTimes.get(player);
		int playtime = arrayTimes.get(0);
		return playtime;
	}
	
}