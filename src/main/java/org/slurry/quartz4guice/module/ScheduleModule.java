package org.slurry.quartz4guice.module;

import org.quartz.SchedulerFactory;
import org.slurry.quartz4guice.aop.ScheduledTypeListener;
import org.slurry.quartz4guice.scheduling.ScheduleFactoryProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

public class ScheduleModule extends AbstractModule {
	protected void configure() {

		bind(SchedulerFactory.class).toProvider(getScheduleFactoryProvider())
				.asEagerSingleton();

		ScheduledTypeListener scheduledTypeListener = new ScheduledTypeListener();
		requestInjection(scheduledTypeListener);
		bindListener(Matchers.any(), scheduledTypeListener);
	}

	protected Class<? extends Provider<SchedulerFactory>> getScheduleFactoryProvider() {

		return ScheduleFactoryProvider.class;
	}

}
