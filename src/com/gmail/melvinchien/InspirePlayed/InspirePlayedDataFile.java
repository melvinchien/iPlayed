package com.gmail.melvinchien.InspirePlayed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.bukkit.entity.Player;

public class InspirePlayedDataFile {
	static Double playtime;
	static String player;

	// Load existing data
	public static void loadMain() {
		String dataFile = InspirePlayed.mainDirectory + "playtimes.txt";
		InspirePlayedData data = new InspirePlayedData(dataFile);

		data.load();

		player = data.getString("Player", "");
		playtime = data.getDouble("Playtime", 0);

		data.save("InspiredPlayed Data File");

	}
	
	// Does player exist
	public static boolean containsKey(Player p, File file) {
		Properties properties = new Properties();
		String player = p.getName();
		try {
			FileInputStream inText = new FileInputStream(file);
			properties.load(inText);
			if (properties.containsKey(player))
				return true;
		} catch (IOException e) {
		
		}
		return false;
	}
	
	
	public static void write(Player p, Double time, int login, File file) {
		Properties properties = new Properties();
		String player = p.getName();
		String playtime = (new Double(time)).toString();
		String lastlogin = (new Integer(login)).toString();
		try {
			FileInputStream inText = new FileInputStream(file);
			properties.load(inText);
			//properties.setProperty(arg0, arg1)(player, playtime, lastlogin);
			properties.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			//
		}
	}
}
