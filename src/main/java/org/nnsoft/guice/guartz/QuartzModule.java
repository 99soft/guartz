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
 * Quartz (http://www.quartz-scheduler.org/) Module as Google-Guice extension.
 */
public abstract class QuartzModule
    extends AbstractModule
{

    private Multibinder<JobListener> jobListeners;

    private Multibinder<TriggerListener> triggerListeners;

    private Multibinder<SchedulerListener> schedulerListeners;

	private SchedulerConfiguration schedulerConfiguration;

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configure()
    {
        checkState( jobListeners == null, "Re-entry is not allowed." );
        checkState( triggerListeners == null, "Re-entry is not allowed." );
        checkState( schedulerListeners == null, "Re-entry is not allowed." );
        checkState( schedulerConfiguration == null, "Re-entry is not allowed." );

        jobListeners = newSetBinder( binder(), JobListener.class );
        triggerListeners = newSetBinder( binder(), TriggerListener.class );
        schedulerListeners = newSetBinder( binder(), SchedulerListener.class );
        schedulerConfiguration = new SchedulerConfiguration();

        try
        {
        	bindScope(QuartzJobScoped.class, QuartzScopes.QUARTZ_JOB);
        	bind(QuartzJobScope.class).toInstance(QuartzScopes.QUARTZ_JOB);
            schedule();
            bind( JobFactory.class ).to( InjectorJobFactory.class ).in( SINGLETON );
            bind( Scheduler.class ).toProvider( SchedulerProvider.class ).asEagerSingleton();
            bind( SchedulerConfiguration.class ).toInstance( schedulerConfiguration );
        }
        finally
        {
            jobListeners = null;
            triggerListeners = null;
            schedulerListeners = null;
            schedulerConfiguration = null;
        }
    }

    /**
     * Part of the EDSL builder language for configuring {@code Job}s.
     * Here is a typical example of scheduling {@code Job}s when creating your Guice injector:
     *
     * <pre>
     * Guice.createInjector(..., new QuartzModule() {
     *
     *     {@literal @}Override
     *     protected void schedule() {
     *       <b>scheduleJob(MyJobImpl.class).withCronExpression("0/2 * * * * ?");</b>
     *     }
     *
     * });
     * </pre>
     *
     * @see JobSchedulerBuilder
     */
    protected abstract void schedule();

    /**
     * Allows to configure the scheduler.
     *
     * <pre>
     * Guice.createInjector(..., new QuartzModule() {
     *
     *     {@literal @}Override
     *     protected void schedule() {
     *       configureScheduler().withManualStart().withProperties(...);
     *     }
     *
     * });
     * </pre>
     *
     * @since 1.1
     */
    protected final SchedulerConfigurationBuilder configureScheduler()
    {
        return schedulerConfiguration;
    }

    /**
     * Add the {@code JobListener} binding.
     *
     * @param jobListenerType The {@code JobListener} class has to be bound
     */
    protected final void addJobListener( Class<? extends JobListener> jobListenerType )
    {
        doBind( jobListeners, jobListenerType );
    }

    /**
     * Add the {@code TriggerListener} binding.
     *
     * @param triggerListenerType The {@code TriggerListener} class has to be bound
     */
    protected final void addTriggerListener( Class<? extends TriggerListener> triggerListenerType )
    {
        doBind( triggerListeners, triggerListenerType );
    }

    /**
     * Add the {@code SchedulerListener} binding.
     *
     * @param schedulerListenerType The {@code SchedulerListener} class has to be bound
     */
    protected final void addSchedulerListener( Class<? extends SchedulerListener> schedulerListenerType )
    {
        doBind( schedulerListeners, schedulerListenerType );
    }

    /**
     * Allows {@code Job} scheduling, delegating Guice create the {@code Job} instance
     * and inject members.
     *
     * If given {@code Job} class is annotated with {@link Scheduled}, then {@code Job}
     * and related {@code Trigger} values will be extracted from it.
     *
     * @param jobClass The {@code Job} has to be scheduled
     * @return The {@code Job} builder
     */
    protected final JobSchedulerBuilder scheduleJob( Class<? extends Job> jobClass )
    {
        checkNotNull( jobClass, "Argument 'jobClass' must be not null." );

        JobSchedulerBuilder builder = new JobSchedulerBuilder( jobClass );

        if ( jobClass.isAnnotationPresent( Scheduled.class ) )
        {
            Scheduled scheduled = jobClass.getAnnotation( Scheduled.class );

            builder
            // job
            .withJobName( scheduled.jobName() )
            .withJobGroup( scheduled.jobGroup() )
            .withRequestRecovery( scheduled.requestRecovery() )
            .withStoreDurably( scheduled.storeDurably() )
            // trigger
            .withCronExpression( scheduled.cronExpression() )
            .withTriggerName( scheduled.triggerName() );

            if ( !DEFAULT.equals( scheduled.timeZoneId() ) )
            {
                TimeZone timeZone = getTimeZone( scheduled.timeZoneId() );
                if ( timeZone != null )
                {
                    builder.withTimeZone( timeZone );
                }
            }
        }

        requestInjection( builder );
        return builder;
    }

    /**
     * Utility method to respect the DRY principle.
     *
     * @param <T>
     * @param binder
     * @param type
     */
    protected final <T> void doBind( Multibinder<T> binder, Class<? extends T> type )
    {
        checkNotNull( type );
        binder.addBinding().to( type );
    }

}
