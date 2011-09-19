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

import com.mini.*;

public class iPlayedPlayerListener extends PlayerListener {
	private final iPlayed plugin;

	public iPlayedPlayerListener(iPlayed instance) {
		plugin = instance;
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		login(event.getPlayer());
	}

	public void onPlayerKick(PlayerKickEvent event) {
		resetStartTime(event.getPlayer());
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		logout(event.getPlayer());
	}

	private void login (Player p) {
		String player = p.getName();
		Arguments entry = null;
		if (plugin.timesdb.hasIndex(player))
			entry = plugin.timesdb.getArguments(player);
		else {
			entry = new Arguments(player);
			entry.setValue("playtime", "0");
			plugin.timesdb.addIndex(player, entry);
			plugin.timesdb.update();
		}
		DateTime dt = new DateTime();
		plugin.timesdb.setArgument(player, "lastlogin", dt.getMonthOfYear() + "/" + 
				dt.getDayOfMonth() + "/" + dt.getYear(), true);
		plugin.timesdb.setArgument(player, "startTime", Integer.toString(dt.getMinuteOfDay()), true);
	}

	private void logout (Player p) {
		String player = p.getName();
		Arguments entry = plugin.timesdb.getArguments(player);
		DateTime dt = new DateTime();
		int startTime = entry.getInteger("startTime");
		int endTime = dt.getMinuteOfDay();
		int playtime = entry.getInteger("playtime");
		if (endTime <= startTime)
			playtime += 1440 - startTime + endTime;
		else
			playtime += endTime - startTime;
		plugin.timesdb.setArgument(player, "playtime", playtime, true);
		plugin.timesdb.setArgument(player, "startTime", "0", true);
	}

	private void resetStartTime (Player p) {
		String player = p.getName();
		DateTime dt = new DateTime();
		plugin.timesdb.setArgument(player, "startTime", Integer.toString(dt.getMinuteOfDay()), true);
	}
}