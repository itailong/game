package com.starland.xyqp.common.component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.starland.tools.network.RouteSession;

@Component
public class SessionManager {

	private Map<String, RouteSession> sessionMap = new ConcurrentHashMap<>();

	public int size() {
		return sessionMap.size();
	}

	public boolean isEmpty() {
		return sessionMap.isEmpty();
	}

	public boolean contains(Object id) {
		return sessionMap.containsKey(id);
	}

	public RouteSession get(String id) {
		return sessionMap.get(id);
	}

	public RouteSession add(RouteSession session) {
		String id = session.getId();
		return sessionMap.put(id, session);
	}

	public RouteSession remove(String id) {
		return sessionMap.remove(id);
	}

	public Collection<RouteSession> values() {
		return sessionMap.values();
	}
	
}
