/*
 *    Copyright 2009-2011 The 99 Software Foundation
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
package org.nnsoft.guice.guartz.internal;

import static com.google.inject.internal.util.$Preconditions.checkArgument;
import static com.google.inject.internal.util.$Preconditions.checkNotNull;
import static com.google.inject.internal.util.$Preconditions.checkState;
import static com.google.inject.multibindings.Multibinder.newSetBinder;

import org.nnsoft.guice.guartz.Global;
import org.nnsoft.guice.guartz.Scheduled;
import org.quartz.Job;
import org.quartz.JobListener;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * 
 */
public abstract class AbstractGuartzModule extends AbstractModule {

    private Multibinder<JobListener> globalJobListeners;

    private Multibinder<JobListener> jobListeners;

    private Multibinder<TriggerListener> globalTriggerListeners;

    private Multibinder<TriggerListener> triggerListeners;

    private Multibinder<SchedulerListener> schedulerListeners;

    private Multibinder<Job> jobClasses;

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configure() {
        checkState(globalJobListeners == null, "Re-entry is not allowed.");
        checkState(jobListeners == null, "Re-entry is not allowed.");
        checkState(globalTriggerListeners == null, "Re-entry is not allowed.");
        checkState(triggerListeners == null, "Re-entry is not allowed.");
        checkState(schedulerListeners == null, "Re-entry is not allowed.");
        checkState(jobClasses == null, "Re-entry is not allowed.");

        globalJobListeners = newSetBinder(binder(), JobListener.class, Global.class);
        jobListeners = newSetBinder(binder(), JobListener.class);
        globalTriggerListeners = newSetBinder(binder(), TriggerListener.class, Global.class);
        triggerListeners = newSetBinder(binder(), TriggerListener.class);
        schedulerListeners = newSetBinder(binder(), SchedulerListener.class);
        jobClasses = newSetBinder(binder(), Job.class);

        try {
            configureGuartz();
            bind(JobFactory.class).to(InjectorJobFactory.class);
        } finally {
            globalJobListeners = null;
            jobListeners = null;
            globalTriggerListeners = null;
            triggerListeners = null;
            schedulerListeners = null;
            jobClasses = null;
        }
    }

    protected abstract void configureGuartz();

    /**
     * 
     *
     * @param jobListenerType
     */
    protected final void addGlobalJobListener(Class<? extends JobListener> jobListenerType) {
        doBind(globalJobListeners, jobListenerType);
    }

    /**
     * 
     *
     * @param jobListenerType
     */
    protected final void addJobListener(Class<? extends JobListener> jobListenerType) {
        doBind(jobListeners, jobListenerType);
    }

    /**
     * 
     *
     * @param triggerListenerType
     */
    protected final void addGlobalTriggerListener(Class<? extends TriggerListener> triggerListenerType) {
        doBind(globalTriggerListeners, triggerListenerType);
    }

    /**
     * 
     *
     * @param triggerListenerType
     */
    protected final void addTriggerListener(Class<? extends TriggerListener> triggerListenerType) {
        doBind(triggerListeners, triggerListenerType);
    }

    /**
     * 
     * @param schedulerListenerType
     */
    protected final void addSchedulerListener(Class<? extends SchedulerListener> schedulerListenerType) {
        doBind(schedulerListeners, schedulerListenerType);
    }

    /**
     * 
     *
     * @param jobClass
     */
    protected final void addJob(Class<? extends Job> jobClass) {
        checkNotNull(jobClass, "Argument 'jobClass' must be not null.");
        checkArgument(jobClass.isAnnotationPresent(Scheduled.class),
                "This method allows only '%s' implementations annotated with @%s",
                Job.class.getName(),
                Scheduled.class.getName());
        doBind(jobClasses, jobClass);
    }

    /**
     * 
     *
     * @param <T>
     * @param binder
     * @param type
     */
    protected final <T> void doBind(Multibinder<T> binder, Class<? extends T> type) {
        checkNotNull(type);
        binder.addBinding().to(type);
    }

}
