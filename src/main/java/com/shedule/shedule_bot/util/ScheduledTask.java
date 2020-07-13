package com.shedule.shedule_bot.util;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

	@Scheduled(fixedRate = 10 * 1000)
	public void scheduleFixedDelayTask() {
		System.out.println("memory : " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()) / 1024 );
	}

	@Scheduled(fixedRate = 60 * 1000)
	public void gc() {
		System.gc();
	}
}
