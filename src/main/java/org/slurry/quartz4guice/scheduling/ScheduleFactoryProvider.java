package org.slurry.quartz4guice.scheduling;

import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.google.inject.Provider;

public class ScheduleFactoryProvider implements Provider<SchedulerFactory> {

    private final SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public SchedulerFactory get() {
        return this.schedulerFactory;
    }

}
