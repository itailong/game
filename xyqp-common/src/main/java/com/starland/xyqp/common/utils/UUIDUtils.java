package com.starland.xyqp.common.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String result = uuid.toString();
		return result.replaceAll("\\-", "");
	}
	
}
