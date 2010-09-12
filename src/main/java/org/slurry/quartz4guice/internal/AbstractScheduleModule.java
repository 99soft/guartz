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
package org.slurry.quartz4guice.internal;

import java.util.HashSet;
import java.util.Set;

import org.quartz.Job;
import org.quartz.JobListener;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;
import org.slurry.quartz4guice.Global;
import org.slurry.quartz4guice.Scheduled;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

/**
 * 
 *
 * @version $Id$
 */
public abstract class AbstractScheduleModule extends AbstractModule {

    private final Set<Class<? extends JobListener>> globalJobListeners = new HashSet<Class<? extends JobListener>>();

    private final Set<Class<? extends JobListener>> jobListeners = new HashSet<Class<? extends JobListener>>();

    private final Set<Class<? extends TriggerListener>> globalTriggerListeners = new HashSet<Class<? extends TriggerListener>>();

    private final Set<Class<? extends TriggerListener>> triggerListeners = new HashSet<Class<? extends TriggerListener>>();

    private final Set<Class<? extends SchedulerListener>> schedulerListeners = new HashSet<Class<? extends SchedulerListener>>();

    private final Set<Class<? extends Job>> jobClasses = new HashSet<Class<? extends Job>>();

    public final AbstractScheduleModule addGlobalJobListener(Class<? extends JobListener> jobListenerType) {
        return this.addElement(this.globalJobListeners, jobListenerType);
    }

    public final AbstractScheduleModule addJobListener(Class<? extends JobListener> jobListenerType) {
        return this.addElement(this.jobListeners, jobListenerType);
    }

    public final AbstractScheduleModule addGlobalTriggerListener(Class<? extends TriggerListener> triggerListenerType) {
        return this.addElement(this.globalTriggerListeners, triggerListenerType);
    }

    public final AbstractScheduleModule addTriggerListener(Class<? extends TriggerListener> triggerListenerType) {
        return this.addElement(this.triggerListeners, triggerListenerType);
    }

    public final AbstractScheduleModule addSchedulerListener(Class<? extends SchedulerListener> schedulerListenerType) {
        return this.addElement(this.schedulerListeners, schedulerListenerType);
    }

    public AbstractScheduleModule addJob(Class<? extends Job> jobClass) throws Exception {
        if (!jobClass.isAnnotationPresent(Scheduled.class)) {
            throw new Exception("This method allows only '"
                    + Job.class.getName()
                    + "' implementations annotated with @"
                    + Scheduled.class.getName());
        }
        return this.addElement(this.jobClasses, jobClass);
    }

    private <I> AbstractScheduleModule addElement(Set<Class<? extends I>> set, Class<? extends I> type) {
        set.add(type);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure() {
        bind(this.binder(), this.globalJobListeners, JobListener.class, true);
        bind(this.binder(), this.jobListeners, JobListener.class, false);
        bind(this.binder(), this.schedulerListeners, SchedulerListener.class, false);
        bind(this.binder(), this.globalTriggerListeners, TriggerListener.class, true);
        bind(this.binder(), this.triggerListeners, TriggerListener.class, false);

        if (!this.jobClasses.isEmpty()) {
            this.bind(new TypeLiteral<Set<Class<? extends Job>>>(){}).toInstance(this.jobClasses);
        }

        // use the Guice based JobFactory by default
        this.bind(JobFactory.class).to(InjectorJobFactory.class);
    }

    protected static final <I> void bind(Binder binder,
            Set<Class<? extends I>> set,
            Class<I> type, boolean global) {
        if (!set.isEmpty()) {
            Multibinder<I> multibinder;
            if (global) {
                multibinder = Multibinder.newSetBinder(binder, type);
            } else {
                multibinder = Multibinder.newSetBinder(binder, type, Global.class);
            }
            for (Class<? extends I> currentType : set) {
                multibinder.addBinding().to(currentType);
            }
        }
    }

}
