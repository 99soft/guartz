package org.nnsoft.guice.guartz;

/*
 *    Copyright 2009-2012 The 99 Software Foundation
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

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.quartz.JobListener;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;

/**
 * Provides a {@code Scheduler} instance.
 */
final class SchedulerProvider
    implements Provider<Scheduler>
{

    /**
     * The {@code Scheduler} instance will be provided.
     */
    private final Scheduler scheduler;

    /**
     * Initialized a new {@code Provider&lt;Scheduler&gt;} instance.
     *
     * @throws SchedulerException If any error occurs
     */
    @Inject
    public SchedulerProvider(SchedulerConfiguration schedulerConfiguration)
        throws SchedulerException
    {
        this.scheduler = new StdSchedulerFactory().getScheduler();
        if ( !schedulerConfiguration.startManually() )
        {
            this.scheduler.start();
        }
    }

    /**
     * Sets the {@code JobFactory} instance (it will be a {@link InjectorJobFactory} instance).
     *
     * @param jobFactory The {@code JobFactory} instance.
     * @throws SchedulerException If any error occurs
     */
    @Inject
    public void setJobFactory( JobFactory jobFactory )
        throws SchedulerException
    {
        this.scheduler.setJobFactory( jobFactory );
    }

    /**
     * Sets the {@code JobListener}s.
     *
     * @param jobListeners The {@code JobListener}s
     * @throws SchedulerException If any error occurs
     */
    @com.google.inject.Inject( optional = true )
    public void addJobListeners( Set<JobListener> jobListeners )
        throws SchedulerException
    {
        for ( JobListener jobListener : jobListeners )
        {
            this.scheduler.getListenerManager().addJobListener( jobListener );
        }
    }

    /**
     * Sets the {@code SchedulerListener}s.
     *
     * @param schedulerListeners The {@code SchedulerListener}s
     * @throws SchedulerException If any error occurs
     */
    @com.google.inject.Inject( optional = true )
    public void addSchedulerListeners( Set<SchedulerListener> schedulerListeners )
        throws SchedulerException
    {
        for ( SchedulerListener schedulerListener : schedulerListeners )
        {
            this.scheduler.getListenerManager().addSchedulerListener( schedulerListener );
        }
    }

    /**
     * Sets the {@code TriggerListener}s.
     *
     * @param triggerListeners The {@code TriggerListener}s
     * @throws SchedulerException If any error occurs
     */
    @com.google.inject.Inject( optional = true )
    public void addTriggerListeners( Set<TriggerListener> triggerListeners )
        throws SchedulerException
    {
        for ( TriggerListener triggerListener : triggerListeners )
        {
            this.scheduler.getListenerManager().addTriggerListener( triggerListener );
        }
    }

    /**
     * {@inheritDoc}
     */
    public Scheduler get()
    {
        return this.scheduler;
    }

}
