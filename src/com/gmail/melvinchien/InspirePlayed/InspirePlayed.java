package com.gmail.melvinchien.InspirePlayed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.Properties;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

/**
 * Plugin for Bukkit
 * 
 * @author VeiN
 */

public class InspirePlayed extends JavaPlugin implements Serializable{
	private static final long serialVersionUID = 0L;
	private final InspirePlayedPlayerListener playerListener = new InspirePlayedPlayerListener(this);
	public static HashMap<String, ArrayList<Integer>> mapTimes = new HashMap<String, ArrayList<Integer>>();
	private ArrayList<Integer> arrayTimes = new ArrayList<Integer>();
	static String mainDirectory = "plugins/InspirePlayed/";
	static File Playtimes = new File(mainDirectory + "playtimes.dat");
	//static Properties prop = new Properties();
	//int playerCount;
	Logger log = Logger.getLogger("Minecraft");
	public void onEnable() {
		// Load properties
		new File(mainDirectory).mkdir();
		if (!Playtimes.exists()) {
			try{
				Playtimes.createNewFile();
			}catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			mapTimes = loadData(Playtimes);
		}
		/*
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
		 */


		// Register our events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Highest, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Priority.Normal, this);
		//pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, this.playerListener, Event.Priority.Normal, this);

		PluginDescriptionFile pdfFile = this.getDescription();
		log.info(pdfFile.getName() + " " + pdfFile.getVersion() + " is enabled.");
	}

	public void onDisable() {
		saveData(mapTimes, Playtimes);
		log.info("InspirePlayed is disabled.");
	}

	/*
	// Load properties if exists already
	public void loadProcedure() {
		try {
			FileInputStream in = new FileInputStream(Playtimes);
			//prop.load(in);
			//playerCount = Integer.parseInt(prop.getProperty("PlayerCount"));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 */

	// Save data file
	public void saveData(HashMap<String, ArrayList<Integer>> playtimes, File file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(playtimes);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Load data file
	@SuppressWarnings("unchecked")
	public HashMap<String, ArrayList<Integer>> loadData(File file) {
		HashMap<String, ArrayList<Integer>> oldMapTimes = new HashMap<String, ArrayList<Integer>> ();
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			oldMapTimes = (HashMap<String, ArrayList<Integer>>) in.readObject();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oldMapTimes;
	}

	// Check for /played command and display playtime to sender
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("played")) {
			String player;
			String message = "";
			if (args.length == 0) {
				player = ((Player)sender).getName();
				message = "You have played for ";
			} else if (args[0].equalsIgnoreCase("help")) {
				return false;
			} else {
				player = args[0];
				message = player + " has played for ";
			}
			player = player.toLowerCase();
			if (!mapTimes.containsKey(player)) {
				sender.sendMessage(player + " does not exist!");
				return true;
			}
			message += formatPlaytime(getPlaytime(player));
			sender.sendMessage(message);		
			saveData(mapTimes, Playtimes);
			return true;
		}
		return false;
	}

	// Login player and update login time
	// Time in minutes
	public void login(String player) {
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

	// Logout player and update stats
	// Time in minutes
	public void logout(String player) {
		logout(player, 0);
	}

	public void logout(String player, int currentTime) {
		int logout = (int)(System.currentTimeMillis() / 1000L);
		arrayTimes = mapTimes.get(player);
		int login = arrayTimes.get(1);
		int currentPlaytime = (int)((logout - login) / 60);
		int oldPlaytime = arrayTimes.get(0);
		int newPlaytime = oldPlaytime + currentPlaytime;
		arrayTimes.set(0, newPlaytime);
		arrayTimes.set(1, currentTime);
		saveData(mapTimes, Playtimes);
	}

	public void updatePlaytime(String player) {
		int currentTime = (int)(System.currentTimeMillis() / 1000L);
		logout(player, currentTime);
	}

	// Get a player's playtime in minutes
	public int getPlaytime(String player) {
		// Update player's playertime before retrieving
		updatePlaytime(player);
		arrayTimes = mapTimes.get(player);
		int playtime = arrayTimes.get(0);
		return playtime;
	}

	// Format playtime into days, hours, minutes, given playtime in minutes
	public String formatPlaytime (int time) {
		// Calculate days, hours, and minutes
		int timeDays = (int)(time / 60 / 24);
		time -= timeDays * 24 * 60;
		int timeHours = (int)(time / 60);
		time -= timeHours * 60;
		int timeMinutes = time;

		// Determine output text format, empty strings if 0
		String days = "";
		String hours = "";
		String minutes = "";
		if (timeDays == 1)
			days = " day, ";
		else if (timeDays > 1)
			days = " days, ";
		if (timeHours == 1)
			hours = " hour, ";
		else if (timeHours > 1)
			hours = " hours, ";
		if (timeMinutes == 1)  // Special case for minutes
			minutes = " minute."; 
		else
			minutes = " minutes.";  // Will output N minutes or 0 minutes.
		return (timeDays > 0 ? timeDays : "") + days + 
		(timeHours > 0 ? timeHours : "") + hours
		+ timeMinutes + minutes;
	}

}
