package org.slurry.quartz4guice.aop;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.slurry.quartz4guice.annotation.Scheduled;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class ScheduledTypeListener implements TypeListener {

	public <T> void hear(TypeLiteral<T> typeLiteral,
			TypeEncounter<T> typeEncounter) {
		if (typeLiteral.getClass().isAnnotationPresent(Scheduled.class)) {
			startSchedule(typeLiteral.getClass());
		}
	}
	
	private void startSchedule(Class clazz) {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		Scheduler sched = null;
		try {
			sched = schedFact.getScheduler();

			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JobDetail jobDetail = new JobDetail("myJob", // job name
				sched.DEFAULT_GROUP, // job group (you can also specify 'null'
				// to use the default group)
				clazz); // the java class to execute

		Trigger trigger = TriggerUtils.makeDailyTrigger(8, 30);
		trigger.setStartTime(new Date());
		trigger.setName("myTrigger");

		try {
			sched.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
