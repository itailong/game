package com.starland.xyqp.lobby.component;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starland.xyqp.lobby.domain.Turntable;
import com.starland.xyqp.lobby.service.TurntableService;

@Component
public class TaskComponent {

	@Resource
	private TurntableService turntableService;

	/**
	 * 每日定时任务
	 */
	@Scheduled(cron = "0 0 0 ? * *")
	public void day() {
		List<Turntable> list = turntableService.list();
		for (Turntable turntable : list) {
			turntable.setShare(0);
			turntable.setSparrowNum(0);
			turntable.setSparrowShare(0);
			turntable.setLottery(0);
			turntable.setSparrowNum(0);
		}
	}
	
	/**
	 * 每周定时任务
	 */
	@Scheduled(cron = "0 0 0 ? * MON")
	public void run() {
		List<Turntable> list = turntableService.list();
		for (Turntable turntable : list) {
			turntable.setWeekNegative(0);
			turntable.setWeekRoomcard(0);
			turntable.setWeekSparrow(0);
		}
	}
	
	/**
	 * 每月定时任务
	 */
	@Scheduled(cron = "0 0 0 1 * ?")
	public void month() {
		List<Turntable> list = turntableService.list();
		for (Turntable turntable : list) {
			turntable.setMonthNegative(0);
			turntable.setMonthRoomcard(0);
			turntable.setMonthSparrow(0);
		}
	}
	
}
