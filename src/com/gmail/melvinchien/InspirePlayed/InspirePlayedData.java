package com.gmail.melvinchien.InspirePlayed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class InspirePlayedData extends Properties {
	static final long serialVersionUID = 0L;
	private String fileName;

	public InspirePlayedData(String file) {
		this.fileName = file;
	}
	
	// Load
	public void load() {
		File file = new File(this.fileName);
		if (file.exists()) {
			try {
				load(new FileInputStream(this.fileName));
			} catch (IOException e) {
				//
			}
		}
	}
	
	// save
	public void save(String start) {
		try {
			store(new FileOutputStream(this.fileName), start);
		} catch (IOException e) {
			//
		}
	}

	// Return string
	public String getString(String key, String value) {
		if (containsKey(key)) {
			return getProperty(key);
		}
		put(key, String.valueOf(value));
		return value;
	}

	// Return double
	public Double getDouble(String key, double value) {
		if (containsKey(key)) {
			return Double.parseDouble(getProperty(key));
		}
		put(key, String.valueOf(value));
		return value;
	}

}
