package com.starland.xyqp.common.component;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ScheduleFactoryBean  implements FactoryBean<ScheduledExecutorService> {

	@Override
	public ScheduledExecutorService getObject() throws Exception {
		int processors = Runtime.getRuntime().availableProcessors();
		return new ScheduledThreadPoolExecutor(processors * 2 + 1);
	}

	@Override
	public Class<?> getObjectType() {
		return ScheduledExecutorService.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
