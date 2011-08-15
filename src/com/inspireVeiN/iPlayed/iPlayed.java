/**
 * iPlayed - A Bukkit playtime plugin for Minecraft
 * Copyright (C) 2011 Melvin "inspireVeiN" Chien <melvin.chien@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.inspireVeiN.iPlayed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class iPlayed extends JavaPlugin{
	private iPlayedPlayerListener playerListener;
	private PluginDescriptionFile pdf;
	public HashMap<String, iPlayedTime> mapTimes;
	static String mainDirectory = "plugins/iPlayed/";
	static File timesFile = new File(mainDirectory + "playtimes.txt");
	Logger log = Logger.getLogger("Minecraft");

	public void onDisable() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String backupPath = mainDirectory + "playtimes_" + sdf.format(cal.getTime());
		sdf = new SimpleDateFormat("HHmm");
		backupPath += "_" + sdf.format(cal.getTime()) + ".txt";
		File backupFile = new File(backupPath);
		saveData(backupFile);
		saveData(timesFile);
		log.info("[" + pdf.getName() + "] " + pdf.getName() + " " + 
				pdf.getVersion() + " is disabled.");
	}

	public void onEnable() {
		playerListener = new iPlayedPlayerListener(this);
		pdf = this.getDescription();
		mapTimes = new HashMap<String, iPlayedTime>();

		// Create directory and file if they do not already exist
		new File(mainDirectory).mkdir();
		if (!timesFile.exists()) {
			try {
				timesFile.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		else
			loadData();

		// Register events
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_KICK, playerListener, Event.Priority.Monitor, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Monitor, this);

		log.info("[" + pdf.getName() + "] " + pdf.getName() + " " + 
				pdf.getVersion() + " is enabled.");
	}

	public void saveData(File file) {
		try {
			FileWriter fileOut = new FileWriter(file);
			BufferedWriter out = new BufferedWriter (fileOut);
			out.write(pdf.getVersion() + "\n");
			Iterator<Map.Entry<String, iPlayedTime>> mapIt = mapTimes.entrySet().iterator();
			while (mapIt.hasNext()) {
				Map.Entry<String, iPlayedTime> entry = 
					(Map.Entry<String, iPlayedTime>)mapIt.next();
				iPlayedTime it = entry.getValue();
				out.write(entry.getKey() + ";" + it.getPlaytimeRaw() + ";" + it.getLastActiveRaw() + "\n");
			}
			out.close();
			log.info("[" + pdf.getName() + "] " + "Data successfully saved.");
		} catch (IOException e) {e.printStackTrace();}
	}

	public void loadData() {
		try {
			FileReader fileIn = new FileReader(timesFile);
			BufferedReader in = new BufferedReader(fileIn);
			String inLine = in.readLine();
			if (Double.parseDouble(inLine) >= 0.3)
				while (in.ready()) {
					inLine = in.readLine();
					String[] inText = inLine.split(";");
					long playtime = Long.parseLong(inText[1]);
					String lastactive = "0000000000";
					if (inText.length == 3)
						lastactive = inText[2];
					iPlayedTime it = new iPlayedTime(playtime, lastactive);
					put(inText[0], it);
				}
			else
				log.severe("[" + pdf.getName() + "] " + "Error loading data file!");
			in.close();
			log.info("[" + pdf.getName() + "] " + "Data successfully loaded.");
		} catch (IOException e) {e.printStackTrace();}
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("played")) {
			String player;
			boolean online = false;
			if (args.length == 0) {
				player = ((Player)sender).getName();
				online = true;
			} else if (args[0].equalsIgnoreCase("help")) {
				return false;
			} else {
				player = args[0];
			}
			Iterator<Map.Entry<String, iPlayedTime>> mapIt = mapTimes.entrySet().iterator();
			while (mapIt.hasNext()) {
				Map.Entry<String, iPlayedTime> entry = 
					(Map.Entry<String, iPlayedTime>)mapIt.next();
				if (entry.getKey().equalsIgnoreCase(player)) {
					player = entry.getKey();
					iPlayedTime it = entry.getValue();
					if (online)
						it.updatePlaytime();
					sender.sendMessage(ChatColor.GREEN + "[iPlayed] " + ChatColor.RED + 
							player + ChatColor.WHITE + " has played for " + 
							ChatColor.GREEN + it.getPlaytime());
					sender.sendMessage(ChatColor.GREEN + "[iPlayed] " + ChatColor.RED + 
							player + ChatColor.WHITE +  "'s last activity was on " + 
							ChatColor.GREEN + it.getLastActive());
					return true;
				}
			}
			sender.sendMessage(ChatColor.GREEN + "[IP] " + ChatColor.RED + 
					player + ChatColor.WHITE + " does not exist!");
			return true;
		} else if (commandLabel.equalsIgnoreCase("iPlayed")) {
			if (sender.isOp()) {
				if (args.length == 0)
					return false;
				else if (args[0].equalsIgnoreCase("load")) {
					mapTimes.clear();
					loadData();
					sender.sendMessage(ChatColor.GREEN + "[IP] " + 
							ChatColor.WHITE + "Data successfully loaded.");
				} else if (args[0].equalsIgnoreCase("save")) {
					saveData(timesFile);
					sender.sendMessage(ChatColor.GREEN + "[IP] " + 
							ChatColor.WHITE + "Data successfully saved.");
				} else if (args[0].equalsIgnoreCase("version")) {
					sender.sendMessage(ChatColor.GREEN + "[IP] " + 
							ChatColor.WHITE + "Version " + ChatColor.RED + pdf.getVersion() + 
							ChatColor.WHITE + " is currently running.");
				} 
			} else {
				sender.sendMessage(ChatColor.RED + 
				"You do not have permission to use this command!");
			}
			return true;
		}
		return false;
	}

	public boolean contains(String player) {
		return mapTimes.containsKey(player);
	}

	public iPlayedTime get(String player) {
		return mapTimes.get(player);
	}

	public void put(String player, iPlayedTime it) {
		mapTimes.put(player, it);
	}

	public void remove(String player) {
		mapTimes.remove(player);
	}
	
	public void updateAFK(String player) {
		iPlayedTime it = get(player);
		it.updateAFKTimer();
	}
}
