package org.slurry.quartz4guice.module;

import org.quartz.SchedulerFactory;
import org.slurry.quartz4guice.aop.ScheduledTypeListener;
import org.slurry.quartz4guice.scheduling.ScheduleFactoryProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

public final class ScheduleModule extends AbstractModule {

    private final Class<? extends Provider<SchedulerFactory>> schedulerFactoryProviderClass;

    public ScheduleModule() {
        this(ScheduleFactoryProvider.class);
    }

    public ScheduleModule(Class<? extends Provider<SchedulerFactory>> schedulerFactoryProviderClass) {
        this.schedulerFactoryProviderClass = schedulerFactoryProviderClass;
    }

    protected void configure() {
        bind(SchedulerFactory.class).toProvider(this.schedulerFactoryProviderClass).asEagerSingleton();

        ScheduledTypeListener scheduledTypeListener = new ScheduledTypeListener();
        requestInjection(scheduledTypeListener);
        bindListener(Matchers.any(), scheduledTypeListener);
    }

}
