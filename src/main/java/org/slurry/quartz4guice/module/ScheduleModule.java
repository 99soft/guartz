/*
 *    Copyright 2009-2010 The slurry Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.slurry.quartz4guice.module;

import java.util.HashSet;
import java.util.Set;

import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.slurry.quartz4guice.aop.ScheduledTypeListener;
import org.slurry.quartz4guice.scheduling.DefaultSchedulerFactoryProvider;
import org.slurry.quartz4guice.scheduling.InjectorJobFactory;
import org.slurry.quartz4guice.scheduling.SchedulerProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Provider;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

/**
 * 
 * @version $Id$
 */
public final class ScheduleModule extends AbstractModule {

    private final Set<Class<? extends JobListener>> globalJobListeners = new HashSet<Class<? extends JobListener>>();

    private final Set<Class<? extends JobListener>> jobListeners = new HashSet<Class<? extends JobListener>>();

    private final Set<Class<? extends TriggerListener>> globalTriggerListeners = new HashSet<Class<? extends TriggerListener>>();

    private final Set<Class<? extends TriggerListener>> triggerListeners = new HashSet<Class<? extends TriggerListener>>();

    private final Set<Class<? extends SchedulerListener>> schedulerListeners = new HashSet<Class<? extends SchedulerListener>>();

    private final Class<? extends Provider<SchedulerFactory>> schedulerFactoryProviderClass;

    public ScheduleModule() {
        this(DefaultSchedulerFactoryProvider.class);
    }

    public ScheduleModule(Class<? extends Provider<SchedulerFactory>> schedulerFactoryProviderClass) {
        this.schedulerFactoryProviderClass = schedulerFactoryProviderClass;
    }

    public ScheduleModule addGlobalJobListener(Class<? extends JobListener> jobListenerType) {
        return this.addElement(this.globalJobListeners, jobListenerType);
    }

    public ScheduleModule addJobListener(Class<? extends JobListener> jobListenerType) {
        return this.addElement(this.jobListeners, jobListenerType);
    }

    public ScheduleModule addGlobalTriggerListener(Class<? extends TriggerListener> triggerListenerType) {
        return this.addElement(this.globalTriggerListeners, triggerListenerType);
    }

    public ScheduleModule addTriggerListener(Class<? extends TriggerListener> triggerListenerType) {
        return this.addElement(this.triggerListeners, triggerListenerType);
    }

    public ScheduleModule addSchedulerListener(Class<? extends SchedulerListener> schedulerListenerType) {
        return this.addElement(this.schedulerListeners, schedulerListenerType);
    }

    private <I> ScheduleModule addElement(Set<Class<? extends I>> set, Class<? extends I> type) {
        set.add(type);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    protected void configure() {
        bind(SchedulerFactory.class).toProvider(this.schedulerFactoryProviderClass).asEagerSingleton();
        bind(JobFactory.class).to(InjectorJobFactory.class);
        bind(Scheduler.class).toProvider(SchedulerProvider.class);

        bind(this.binder(), this.globalJobListeners, JobListener.class, true);
        bind(this.binder(), this.jobListeners, JobListener.class, false);
        bind(this.binder(), this.schedulerListeners, SchedulerListener.class, false);
        bind(this.binder(), this.globalTriggerListeners, TriggerListener.class, true);
        bind(this.binder(), this.triggerListeners, TriggerListener.class, false);

        bindListener(new JobMatcher(), new ScheduledTypeListener());
    }

    private static <I> void bind(Binder binder, Set<Class<? extends I>> set, Class<I> type, boolean global) {
        if (!set.isEmpty()) {
            Multibinder<I> multibinder = Multibinder.newSetBinder(binder, type);
            for (Class<? extends I> currentType : set) {
                ScopedBindingBuilder bindingBuilder = multibinder.addBinding().to(currentType);
                if (global) {
                    bindingBuilder.in(Global.class);
                }
            }
        }
    }

}
