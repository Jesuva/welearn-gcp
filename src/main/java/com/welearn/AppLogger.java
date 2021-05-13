package com.welearn;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
	static {
		final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		LogManager.getLogManager().reset();
		logger.setLevel(Level.ALL);
		try {
			ConsoleHandler ch = new ConsoleHandler();
			ch.setLevel(Level.ALL);
			logger.addHandler(ch);
		}
		catch(Exception e) {
			System.out.println("Console Logger not Working!");
		}
		
		try {
			FileHandler fh = new FileHandler("E:/Eclipse-Project/MyLogFile.log",true);
			fh.setLevel(Level.ALL);
			fh.setFormatter(new SimpleFormatter());
			logger.addHandler(fh);
		}
		catch(IOException e) {
			System.out.println("File Logger not working!");
		}
		catch(SecurityException e) {
			System.out.println("Access denied for editing file!");
		}		
	
	}
}
