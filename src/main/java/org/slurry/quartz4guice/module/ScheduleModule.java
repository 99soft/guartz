package org.slurry.quartz4guice.module;

import org.slurry.quartz4guice.aop.ScheduledTypeListener;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class ScheduleModule extends AbstractModule {
	protected void configure() {

		ScheduledTypeListener scheduledTypeListener = new ScheduledTypeListener();
		requestInjection(scheduledTypeListener);
		bindListener(Matchers.any(), scheduledTypeListener);
	}

}
