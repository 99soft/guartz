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
package org.nnsoft.guice.guartz;

import static com.google.inject.Scopes.SINGLETON;
import static com.google.inject.internal.util.$Preconditions.checkNotNull;
import static com.google.inject.internal.util.$Preconditions.checkState;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static java.util.TimeZone.getTimeZone;
import static org.nnsoft.guice.guartz.Scheduled.DEFAULT;

import java.util.TimeZone;

import org.quartz.Job;
import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.spi.JobFactory;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * 
 */
public abstract class QuartzModule extends AbstractModule {

    private Multibinder<JobListener> jobListeners;

    private Multibinder<TriggerListener> triggerListeners;

    private Multibinder<SchedulerListener> schedulerListeners;

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configure() {
        checkState(jobListeners == null, "Re-entry is not allowed.");
        checkState(triggerListeners == null, "Re-entry is not allowed.");
        checkState(schedulerListeners == null, "Re-entry is not allowed.");

        jobListeners = newSetBinder(binder(), JobListener.class);
        triggerListeners = newSetBinder(binder(), TriggerListener.class);
        schedulerListeners = newSetBinder(binder(), SchedulerListener.class);

        try {
            schedule();
            bind(JobFactory.class).to(InjectorJobFactory.class).in(SINGLETON);
            bind(Scheduler.class).toProvider(SchedulerProvider.class).asEagerSingleton();
        } finally {
            jobListeners = null;
            triggerListeners = null;
            schedulerListeners = null;
        }
    }

    /**
     * Part of the EDSL builder language for configuring servlets
     * and filters with guice-servlet. Think of this as an in-code replacement for web.xml.
     * Filters and servlets are configured here using simple java method calls. Here is a typical
     * example of registering a filter when creating your Guice injector:
     *
     * <pre>
     * Guice.createInjector(..., new QuartzModule() {
     *
     *     {@literal @}Override
     *     protected void schedule() {
     *       <b>scheduleJob(MyJobImpl.class).withCronExpression("0/2 * * * * ?");</b>
     *     }
     *
     * }
     * </pre>
     */
    protected abstract void schedule();

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
    protected final JobSchedulerBuilder scheduleJob(Class<? extends Job> jobClass) {
        checkNotNull(jobClass, "Argument 'jobClass' must be not null.");

        JobSchedulerBuilder builder = new JobSchedulerBuilder(jobClass);

        if (jobClass.isAnnotationPresent(Scheduled.class)) {
            Scheduled scheduled = jobClass.getAnnotation(Scheduled.class);

            builder
                   // job
                   .withJobName(scheduled.jobName())
                   .withJobGroup(scheduled.jobGroup())
                   .withRequestRecovery(scheduled.requestRecovery())
                   .withStoreDurably(scheduled.storeDurably())
                   // trigger
                   .withCronExpression(scheduled.cronExpression())
                   .withTriggerName(scheduled.triggerName());

            if (!DEFAULT.equals(scheduled.timeZoneId())) {
                TimeZone timeZone = getTimeZone(scheduled.timeZoneId());
                if (timeZone != null) {
                    builder.withTimeZone(timeZone);
                }
            }
        }

        requestInjection(builder);
        return builder;
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
