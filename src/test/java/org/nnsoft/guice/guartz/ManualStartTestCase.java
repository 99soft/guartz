package org.nnsoft.guice.guartz;

import static org.junit.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;

import com.google.inject.Guice;

public class ManualStartTestCase {

	@Inject
	private Scheduler scheduler;
	
    @Inject
    private TimedTask timedTask;
	
    @Before
	public void setUp() {
		Guice.createInjector(new QuartzModule() {
			@Override
			protected void schedule() {
				configureScheduler().withManualStart();
				scheduleJob(TimedTask.class);
			}
		}).injectMembers(this);
	}
	
    @After
	public void tearDown() throws Exception {
		scheduler.shutdown();
	}
	
	@Test
	public void testManualStart() throws Exception {
		Thread.sleep(5000L);
		assertTrue(timedTask.getInvocationsTimedTaskA() == 0);
		scheduler.start();
		Thread.sleep(5000L);
		assertTrue(timedTask.getInvocationsTimedTaskA() > 0);
	}
}
