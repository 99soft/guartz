package org.slurry.quartz4guice;

import org.slurry.quartz4guice.annotation.Scheduled;

public class TimedTasks implements InterfaceContainingTimedTask {

	private Integer invocationsA = 0;
	private Integer invocationsB = 0;

	@Scheduled(cron = "2 * * * * *")
	public void timedTaskA() {
		invocationsA++;

	}

	@Scheduled(cron = "1 * * * * *")
	public void timedTaskB() {
		invocationsB++;

	}

	public int getInvocationsTimedTaskA() {
		return invocationsA;
	}

	public int getInvocationsTimedTaskB() {
		return invocationsB;
	}

}
