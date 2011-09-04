/**
 * iPlayed - A Bukkit playtime plugin for Minecraft
 * Copyright (C) 2011 Melvin "inspireVeiN" Chien <melvin.chien@gmail.com>
 * @author inspireVeiN
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

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.joda.time.DateTime;

import com.mini.Arguments;
import com.mini.Mini;

public class iPlayedPlayerListener extends PlayerListener {
	private final iPlayed plugin;
	private Mini timesdb;

	public iPlayedPlayerListener(iPlayed instance) {
		plugin = instance;
		timesdb = plugin.timesdb;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		login(event.getPlayer());
	}
	
	public void onPlayerKick(PlayerKickEvent event) {
		logout(event.getPlayer());
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		logout(event.getPlayer());
	}
	
	private void login (Player p) {
		String player = p.getName();
		Arguments entry;
		if (timesdb.hasIndex(player))
			entry = timesdb.getArguments(player);
		else {
			entry = new Arguments(player);
			entry.setValue("playtime", "0");
			timesdb.addIndex(entry.getKey(), entry);
		}
		DateTime dt = new DateTime();
		entry.setValue("lastlogin", dt.getMonthOfYear() + "/" + 
				dt.getDayOfMonth() + "/" + dt.getYear());
		entry.setValue("startTime", Integer.toString(dt.getMinuteOfDay()));
	}

	private void logout (Player p) {
		String player = p.getName();
		Arguments entry = timesdb.getArguments(player);
		DateTime dt = new DateTime();
		int startTime = entry.getInteger("startTime");
		int endTime = dt.getMinuteOfDay();
		int playtime = entry.getInteger("playtime");
		if (endTime < startTime)
			playtime += 1440 - startTime + endTime;
		else
			playtime += endTime - startTime;
		entry.setValue("playtime", Integer.toString(playtime));
		entry.setValue("startTime", "");
		timesdb.update();
	}
}