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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class iPlayedTime {

	public iPlayedTime(long playtime, String lastactive) {
		this.playtime = playtime;
		this.lastactive = lastactive;
		this.time = 0;
		this.afktimer = 0;
		this.afk = false;
	}

	public iPlayedTime() {
		this(0L, "0000000000");
	}

	// Log in player and set lastactive and afktime to current time
	public void login() {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyHHmm");
		lastactive = sdf.format(c.getTime());
		time = getCurrentTime();
		afk = false;
		updateAFKTimer();
	}

	// Log out player and update playtime and reset afktime
	public void logout() {
		updatePlaytime();
		time = 0;
		afktimer = 0;
		afk = false;
	}

	// Update the playtime and set lastactive to current time in minutes
	public void updatePlaytime() {
		if (!isAFK()) {
			playtime += afktimer - time;
			login();
		} else {
			time = getCurrentTime();
		}

	}

	// Return current time in minutes
	private static long getCurrentTime() {
		return System.currentTimeMillis() / 60000L;
	}

	// Format playtime to string of days, hours, minutes
	public String getPlaytime() {
		// Calculate days, hours, and minutes
		long tempTime = playtime;
		long timeDays = (long)(tempTime / 60 / 24);
		tempTime -= timeDays * 24 * 60;
		long timeHours = (int)(tempTime / 60);
		tempTime -= timeHours * 60;
		long timeMinutes = tempTime;

		// Determine output text format, empty strings if 0
		String stDays = "";
		String stHours = "";
		String stMinutes = "";
		if (timeDays == 1)
			stDays = " day, ";
		else if (timeDays > 1)
			stDays = " days, ";
		if (timeHours == 1)
			stHours = " hour, ";
		else if (timeHours > 1)
			stHours = " hours, ";
		if (timeMinutes == 1)  // Special case for minutes
			stMinutes = " minute."; 
		else
			stMinutes = " minutes.";  // Will output N minutes or 0 minutes.

		return (timeDays > 0 ? timeDays : "") + stDays + 
		(timeHours > 0 ? timeHours : "") + stHours
		+ timeMinutes + stMinutes;
	}

	public long getPlaytimeRaw() {
		return playtime;
	}

	// Format last login time to string of month, date, year, hours, minutes
	public String getLastActive() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("z");
		return lastactive.substring(0,2) + "/" + 
		lastactive.substring(2,4) + "/" +
		lastactive.substring(4,6) + " at " +
		lastactive.substring(6,8) + ":" +
		lastactive.substring(8,10) + " " +
		sdf.format(cal.getTime()) + ".";
	}

	public String getLastActiveRaw() {
		return lastactive;
	}

	public void updateAFKTimer() {
		afktimer = getCurrentTime();
	}

	public boolean isAFK() {
		if (getCurrentTime() - afktimer >= 5)
			return true;
		return false;
	}

	private long playtime;
	private String lastactive;
	private long time;
	private long afktimer;
	private boolean afk;
}
