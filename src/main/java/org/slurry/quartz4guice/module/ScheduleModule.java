package org.slurry.quartz4guice.module;

import org.slurry.quartz4guice.annotation.Scheduled;
import org.slurry.quartz4guice.aop.ScheduledInterceptor;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class ScheduleModule extends AbstractModule {
	protected void configure() {



		ScheduledInterceptor scheduledInterceptor = new ScheduledInterceptor();
		requestInjection(scheduledInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(Scheduled.class),
				scheduledInterceptor);
	}

}
