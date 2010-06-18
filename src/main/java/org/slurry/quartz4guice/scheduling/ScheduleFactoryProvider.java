package org.slurry.quartz4guice.scheduling;

import org.quartz.SchedulerFactory;

import com.google.inject.Provider;

public class ScheduleFactoryProvider implements Provider<SchedulerFactory> {

	public SchedulerFactory get() {
		return new org.quartz.impl.StdSchedulerFactory();
	}

}
