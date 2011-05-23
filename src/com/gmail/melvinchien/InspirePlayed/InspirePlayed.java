package com.gmail.melvinchien.InspirePlayed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

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
	private static final long serialVersionUID = 1L;
	private final InspirePlayedPlayerListener playerListener = new InspirePlayedPlayerListener(this);
	public static HashMap<Player, ArrayList<Double>> playtimes = new HashMap<Player, ArrayList<Double>>();
	static String mainDirectory = "plugins/InspirePlayed/";
	static File Playtimes = new File(mainDirectory + "playtimes.dat");
	static Properties prop = new Properties();
	int playerCount;
	Logger log = Logger.getLogger("Minecraft");
	

	public void onEnable() {
		// Load properties
		new File(mainDirectory).mkdir();
		if (!Playtimes.exists()) {
			// Create new properties file
			try {
				Playtimes.createNewFile();
				FileOutputStream out = new FileOutputStream(Playtimes);
				prop.put("PlayerCount", "0");
				prop.store(out, "InspirePlayed Config File");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			loadProcedure();
		}
		InspirePlayedDataFile.loadMain();

		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Normal, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}
	
	public void onDisable() {
		log.info("InspirePlayed is disabled!");
	}
	
	// Load properties if exists already
	public void loadProcedure() {
		try {
		FileInputStream in = new FileInputStream(Playtimes);
		prop.load(in);
		playerCount = Integer.parseInt(prop.getProperty("PlayerCount"));
		in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void save (HashMap<Player, ArrayList<Double>> playtimes, String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(playtimes);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
