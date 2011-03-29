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
package org.nnsoft.guice.guartz.core;

import static com.google.inject.internal.util.$Preconditions.checkNotNull;
import static com.google.inject.internal.util.$Preconditions.checkState;
import static com.google.inject.multibindings.Multibinder.newSetBinder;
import static com.google.inject.util.Providers.guicify;

import javax.inject.Provider;

import org.nnsoft.guice.guartz.internal.AbstractGuartzModule;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.core.SchedulingContext;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.InstanceIdGenerator;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.spi.ThreadPool;

import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

/**
 * 
 */
public abstract class GuartzModule extends AbstractGuartzModule {

    private Multibinder<SchedulerPlugin> schedulerPluginTypes;

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void configureGuartz() {
        checkState(schedulerPluginTypes == null, "Re-entry is not allowed.");
        schedulerPluginTypes = newSetBinder(binder(), SchedulerPlugin.class);

        try {
            configureScheduler();

            bind(QuartzSchedulerResources.class).toProvider(QuartzSchedulerResourcesProvider.class);
            bind(SchedulingContext.class).in(Scopes.SINGLETON);
            bind(QuartzScheduler.class).toProvider(QuartzSchedulerProvider.class).asEagerSingleton();
        } finally {
            schedulerPluginTypes = null;
        }
    }

    /**
     * 
     * @param classLoadHelperType
     */
    protected final void bindClassLoadHelper(Class<? extends ClassLoadHelper> classLoadHelperType) {
        checkNotNull(classLoadHelperType, "Argument 'classLoadHelperType' must be not null.");
        bind(ClassLoadHelper.class).to(classLoadHelperType);
    }

    /**
     * 
     * @param instanceIdGeneratorType
     */
    protected final void bindInstanceIdGeneratorType(Class<? extends InstanceIdGenerator> instanceIdGeneratorType) {
        checkNotNull(instanceIdGeneratorType, "Argument 'instanceIdGeneratorType' must be not null.");
        bind(InstanceIdGenerator.class).to(instanceIdGeneratorType);
    }

    /**
     * 
     * @param jobStoreType
     */
    protected final void bindJobStoreType(Class<? extends JobStore> jobStoreType) {
        checkNotNull(jobStoreType, "Argument 'jobStoreType' must be not null.");
        bind(JobStore.class).to(jobStoreType);
    }

    /**
     * 
     * @param schedulerSignalerProviderType
     */
    protected final void bindSchedulerSignalerProviderType(Class<? extends Provider<SchedulerSignaler>> schedulerSignalerProviderType) {
        checkNotNull(schedulerSignalerProviderType, "Argument 'schedulerSignalerProviderType' must be not null.");
        bind(SchedulerSignaler.class).toProvider(schedulerSignalerProviderType);
    }

    /**
     * 
     * @param schedulerSignalerProvider
     */
    protected final void bindSchedulerSignalerProvider(Provider<SchedulerSignaler> schedulerSignalerProvider) {
        checkNotNull(schedulerSignalerProvider, "Argument 'schedulerSignalerProviderType' must be not null.");
        bind(SchedulerSignaler.class).toProvider(guicify(schedulerSignalerProvider));
    }

    /**
     * 
     * @param threadPoolProviderType
     */
    protected final void bindThreadPoolProviderType(Class<? extends Provider<ThreadPool>> threadPoolProviderType) {
        checkNotNull(threadPoolProviderType, "Argument 'threadPoolProviderType' must be not null.");
        bind(ThreadPool.class).toProvider(threadPoolProviderType);
    }

    /**
     * 
     * @param threadPoolProvider
     */
    protected final void bindThreadPoolProviderType(Provider<ThreadPool> threadPoolProvider) {
        checkNotNull(threadPoolProvider, "Argument 'threadPoolProvider' must be not null.");
        bind(ThreadPool.class).toProvider(guicify(threadPoolProvider));
    }

    /**
     * 
     * @param schedulerPluginType
     */
    protected final void addSchedulerPluginType(Class<? extends SchedulerPlugin> schedulerPluginType) {
        doBind(schedulerPluginTypes, schedulerPluginType);
    }

    /**
     * 
     */
    protected abstract void configureScheduler();

}
