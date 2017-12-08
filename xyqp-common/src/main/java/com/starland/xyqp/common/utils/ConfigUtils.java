package com.starland.xyqp.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigUtils {

	private static Properties properties = new Properties();

	private static final Logger LOGGER = LogManager.getLogger(ConfigUtils.class);
	
	static {
		try (InputStream in = ConfigUtils.class.getResourceAsStream("/config.properties");
				InputStreamReader reader = new InputStreamReader(in, "UTF-8")) {
			properties.load(reader);
		} catch (IOException e) {
			LOGGER.error("", e);
		}
	}

	public static String getProperty(String name) {
		return properties.getProperty(name);
	}
	
}
