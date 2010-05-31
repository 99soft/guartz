package org.slurry.quartz4guice.aop;

import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

public class ScheduledInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation invocation) throws Throwable {
		SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

		Scheduler sched = schedFact.getScheduler();

		sched.start();

		JobDetail jobDetail = new JobDetail("myJob", // job name
				sched.DEFAULT_GROUP, // job group (you can also specify 'null'
										// to use the default group)
				DumbJob.class); // the java class to execute

		Trigger trigger = TriggerUtils.makeDailyTrigger(8, 30);
		trigger.setStartTime(new Date());
		trigger.setName("myTrigger");

		sched.scheduleJob(jobDetail, trigger);

		return invocation.proceed();

	}

}
