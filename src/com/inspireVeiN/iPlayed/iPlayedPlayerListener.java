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

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
//import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class iPlayedPlayerListener extends PlayerListener {
	private final iPlayed plugin;

	public iPlayedPlayerListener(iPlayed instance) {
		plugin = instance;
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		String player = event.getPlayer().getName();

		if (plugin.contains(player))
			plugin.get(player).login();
		else if (plugin.contains(player.toLowerCase())) {
			plugin.put(player, plugin.get(player.toLowerCase()));
			plugin.remove(player.toLowerCase());
			plugin.get(player).login();
		} else {
			iPlayedTime it = new iPlayedTime();
			it.login();
			plugin.put(player, it);
		}
	}

	public void onPlayerKick(PlayerKickEvent event) {
		String player = event.getPlayer().getName();
		plugin.get(player).logout();
		plugin.saveData(plugin.timesFile);
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		String player = event.getPlayer().getName();
		plugin.mapTimes.get(player).logout();
		plugin.saveData(plugin.timesFile);
	}
	
	public void onPlayerMove(PlayerMoveEvent event) {
		String player = event.getPlayer().getName();
		plugin.updateAFK(player);
	}

}