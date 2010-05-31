package org.slurry.quartz4guice;



import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slurry.quartz4guice.module.ScheduleModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.internal.Stopwatch;

public class Quartz4GuiceTimerTest {
	

	private Injector injector;

	private InterfaceContainingTimedTask timedTask;

	
	private Logger logger;

	@Before
	public void beforeTest() {
		
		logger=LoggerFactory.getLogger(Quartz4GuiceTimerTest.class);

		injector = Guice.createInjector(new ScheduleModule(), new GuiceModule());

		timedTask = injector.getInstance(InterfaceContainingTimedTask.class);

	}

	@Test
	public void minimalTest() throws InterruptedException {
		
		Stopwatch stopwatch=new Stopwatch();
		
		
		Thread.sleep(5000);

		Assert.assertEquals(2,  timedTask.getInvocationsTimedTaskA());
		
		logger.debug("Done checking task A {} ms ",stopwatch.reset());

		stopwatch=new Stopwatch();
		
		Assert.assertEquals(5,  timedTask.getInvocationsTimedTaskB());
		
		logger.debug("Done checking task B  {} ms ",stopwatch.reset());
		

	}
	


	@After
	public void afterTest() {

	}

}
