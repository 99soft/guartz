package org.slurry.quartz4guice.aop;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
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
	
	private <T> void startSchedule(Class<T> jobClass) {
	    if (!Job.class.isAssignableFrom(jobClass)) {
	        throw new RuntimeException("Class '"
	                + jobClass.getName()
	                + "' is not a '"
	                + Job.class.getName()
	                + "' instance");
	    }

	    Scheduled scheduled = (Scheduled) jobClass.getAnnotation(Scheduled.class);

		Scheduler sched = null;
		try {
			sched = schedulerFactory.getScheduler();

			sched.start();
		} catch (SchedulerException e) {
			throw new RuntimeException("An error occurred while retrieving a Scheduler instance", e);
		}

		JobDetail jobDetail = new JobDetail(scheduled.jobName(), // job name
		        scheduled.group(), // job group (you can also specify 'null'
				// to use the default group)
				jobClass,
				scheduled.volatility(),
				scheduled.durability(),
				scheduled.recover()); // the java class to execute

		CronTrigger trigger = new CronTrigger(jobClass.getCanonicalName());

		try {
			trigger.setCronExpression(scheduled.cronExpression());
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
		    throw new RuntimeException("An error occurred while scheduling the Job '"
		            + jobDetail
		            + "' instance using cron expression '"
		            + scheduled.cronExpression()
		            + "'", e);
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
