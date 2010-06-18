package org.slurry.quartz4guice.aop;

import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;
import org.slurry.quartz4guice.annotation.Scheduled;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class ScheduledTypeListener implements TypeListener {

	private SchedulerFactory schedulerFactory;
	
	public <T> void hear(TypeLiteral<T> typeLiteral,
			TypeEncounter<T> typeEncounter) {
		if (typeLiteral.getRawType().isAnnotationPresent(Scheduled.class)) {
			startSchedule(typeLiteral.getRawType());
		}
	}
	
	private void startSchedule(Class clazz) {

		Scheduler sched = null;
		try {
			sched = schedulerFactory.getScheduler();

			sched.start();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JobDetail jobDetail = new JobDetail("myJob", // job name
				sched.DEFAULT_GROUP, // job group (you can also specify 'null'
				// to use the default group)
				clazz); // the java class to execute

		CronTrigger trigger = new CronTrigger(clazz.getCanonicalName());
		
		Scheduled scheduled =(Scheduled) clazz.getAnnotation(Scheduled.class);
		
		String cronString=scheduled.cron();
		try {
			trigger.setCronExpression(cronString);
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	@Inject
	public void setSchedulerFactory(SchedulerFactory schedulerFactory) {
		this.schedulerFactory = schedulerFactory;
	}

	public SchedulerFactory getSchedulerFactory() {
		return schedulerFactory;
	}

}
