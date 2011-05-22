package com.gmail.melvinchien.InspirePlayed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * Plugin for Bukkit
 * 
 * @author VeiN
 */

public class InspirePlayed extends JavaPlugin {
	private final InspirePlayedPlayerListener playerListener = new InspirePlayedPlayerListener(this);
	static String directory = "plugins/InspirePlayed/";
	static File playtimes = new File(directory + "playtimes.db");

	

	public void onEnable() {
		new File(directory).mkdir();
		if (!playtimes.exists()) {
			try {
				playtimes.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		InspirePlayedDataFile.loadMain();

		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Normal, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	public void onDisable() {
		System.out.println("InspirePlayed disabled!");
	}
}
