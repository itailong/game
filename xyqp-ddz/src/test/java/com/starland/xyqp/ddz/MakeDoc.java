package com.starland.xyqp.ddz;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.starland.tools.network.RouteSession;
import com.starland.tools.network.annotation.MessageReceiver;
import com.starland.tools.network.annotation.RouteMapping;

public class MakeDoc {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-server.xml", "spring-db.xml");
		String[] names = applicationContext.getBeanNamesForAnnotation(MessageReceiver.class);
		for (String name : names) {
			Object bean = applicationContext.getBean(name);
			Method[] methods = bean.getClass().getMethods();
			for (Method method : methods) {
				RouteMapping routeMapping = method.getAnnotation(RouteMapping.class);
				if (null == routeMapping) {
					continue;
				}
				String code = routeMapping.value();
				Class<?>[] parameterTypes = method.getParameterTypes();
				for (Class<?> clazz : parameterTypes) {
					if (!clazz.isAssignableFrom(RouteSession.class)) {
						make(code, clazz, method);
						continue;
					}
				}
			}
		}
		applicationContext.close();
	}
	
	private static void make(String code, Class<?> clazz, Method method) {
		StringBuilder buf = new StringBuilder();
		buf.append(code + "\t" + method.getName() + "\t");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			buf.append("\t" + field.getName());
		}
		System.out.println(buf);
	}
	
}
