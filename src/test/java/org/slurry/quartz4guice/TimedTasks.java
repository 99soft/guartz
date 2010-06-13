package org.slurry.quartz4guice;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slurry.quartz4guice.annotation.Scheduled;

/*0/15*/
@Scheduled(cron = "0/3 * * * * ?")
public class TimedTasks implements InterfaceContainingTimedTask, Job {

	private static Integer invocationsA = 0;

	public void timedTaskA() {
		invocationsA++;

	}
	public int getInvocationsTimedTaskA() {
		return invocationsA;
	}
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		timedTaskA();
		
	}


}
