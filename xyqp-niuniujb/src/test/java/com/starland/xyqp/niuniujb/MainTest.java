package com.starland.xyqp.niuniujb;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.starland.xyqp.common.component.GameServerComponent;

public class MainTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-db.xml", "spring-server.xml");
		GameServerComponent gameServerComponent = applicationContext.getBean(GameServerComponent.class);
		gameServerComponent.startAll();
		applicationContext.close();
	}
}
