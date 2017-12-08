package com.starland.xyqp.lobby.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		String result = uuid.toString();
		return result.replaceAll("-", "");
	}
	
}
