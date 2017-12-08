package com.starland.xyqp.common.component;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.starland.tools.network.NettyServer;
import com.starland.tools.network.RouteInterceptor;
import com.starland.tools.network.socket.SocketChildHandler;
import com.starland.tools.network.websocket.WebSocketChildHandler;
import com.starland.xyqp.common.exception.ConfigException;
import com.starland.xyqp.db.domain.GameServer;
import com.starland.xyqp.db.service.GameServerService;

public class GameServerComponent implements ApplicationContextAware {

	@Resource
	private GameServerService gameServerService;
	
	@Resource
	private ScheduledExecutorService executorService;
	
	private GameServer gameServer;
	
	private ApplicationContext applicationContext;
	
	private List<RouteInterceptor> interceptors;
	
	private int serverId;

	private static final Logger LOGGER = LogManager.getLogger();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public void setInterceptors(List<RouteInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	@PostConstruct
	public void init() {
		gameServer = gameServerService.get(serverId);
		if (null == gameServer) {
			throw new ConfigException("服务器[" + serverId + "]为空！");
		}
		executorService.scheduleAtFixedRate(this::refreshServer, 10, 60, TimeUnit.SECONDS);
	}
	
	private void refreshServer() {
		Integer id = gameServer.getId();
		gameServerService.updateRefreshTime(id);
	}
	
	public GameServer getGameServer() {
		return gameServer;
	}
	
	public void startAll() {
		new Thread(this::startSocket).start();
		startWebSocket();
	}
	
	public void startSocket() {
		try {
			Integer serverPort = gameServer.getServerPort();
			if (null != serverPort && 0 != serverPort.intValue()) {
				NettyServer nettyServer = new NettyServer();
				nettyServer.setChildHandler(new SocketChildHandler());
				nettyServer.setApplicationContext(applicationContext);
				nettyServer.afterPropertiesSet();
				nettyServer.setExecutorService(executorService);
				nettyServer.setInterceptors(interceptors);
				nettyServer.setPort(serverPort);
				nettyServer.start();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
	public void startWebSocket() {
		try {
			Integer serverPort = gameServer.getWebServerPort();
			if (null != serverPort && 0 != serverPort.intValue()) {
				NettyServer nettyServer = new NettyServer();
				nettyServer.setChildHandler(new WebSocketChildHandler());
				nettyServer.setApplicationContext(applicationContext);
				nettyServer.afterPropertiesSet();
				nettyServer.setExecutorService(executorService);
				nettyServer.setInterceptors(interceptors);
				nettyServer.setPort(serverPort);
				nettyServer.start();
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
}
