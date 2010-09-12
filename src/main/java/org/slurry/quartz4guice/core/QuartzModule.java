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
package org.slurry.quartz4guice.core;

import java.util.HashSet;
import java.util.Set;

import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.core.SchedulingContext;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.InstanceIdGenerator;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.spi.ThreadPool;
import org.slurry.quartz4guice.internal.AbstractScheduleModule;

import com.google.inject.Provider;
import com.google.inject.Scopes;

/**
 * 
 * @version $Id$
 */
public final class QuartzModule extends AbstractScheduleModule {

    private final Set<Class<? extends SchedulerPlugin>> schedulerPluginTypes = new HashSet<Class<? extends SchedulerPlugin>>();

    private Class<? extends ClassLoadHelper> classLoadHelperType;

    private Class<? extends InstanceIdGenerator> instanceIdGeneratorType;

    private Class<? extends JobStore> jobStoreType;

    private Class<? extends Provider<SchedulerSignaler>> schedulerSignalerProviderType;

    private Class<? extends Provider<ThreadPool>> threadPoolProviderType;

    public QuartzModule setClassLoadHelperType(
            Class<? extends ClassLoadHelper> classLoadHelperType) {
        this.classLoadHelperType = classLoadHelperType;
        return this;
    }

    public QuartzModule setInstanceIdGeneratorType(
            Class<? extends InstanceIdGenerator> instanceIdGeneratorType) {
        this.instanceIdGeneratorType = instanceIdGeneratorType;
        return this;
    }

    public QuartzModule setJobStoreType(Class<? extends JobStore> jobStoreType) {
        this.jobStoreType = jobStoreType;
        return this;
    }

    public QuartzModule addSchedulerPluginType(
            Class<? extends SchedulerPlugin> schedulerPluginType) {
        this.schedulerPluginTypes.add(schedulerPluginType);
        return this;
    }

    public QuartzModule setSchedulerSignalerProviderType(
            Class<? extends Provider<SchedulerSignaler>> schedulerSignalerProviderType) {
        return this.assign(schedulerSignalerProviderType, this.schedulerSignalerProviderType);
    }

    public QuartzModule setThreadPoolProviderType(
            Class<? extends Provider<ThreadPool>> threadPoolProviderType) {
        return this.assign(threadPoolProviderType, this.threadPoolProviderType);
    }

    private <I> QuartzModule assign(Class<? extends Provider<I>> source, Class<? extends Provider<I>> dest) {
        if (source == null) {
            throw new IllegalArgumentException("Provider can't be null");
        }
        dest = source;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    protected void configure() {
        super.configure();

        this.bind(QuartzSchedulerResources.class).toProvider(QuartzSchedulerResourcesProvider.class);
        this.bind(SchedulingContext.class).in(Scopes.SINGLETON);
        this.bind(QuartzScheduler.class).toProvider(QuartzSchedulerProvider.class).asEagerSingleton();

        if (this.classLoadHelperType != null) {
            this.bind(ClassLoadHelper.class).to(this.classLoadHelperType);
        }
        if (this.instanceIdGeneratorType != null) {
            this.bind(InstanceIdGenerator.class).to(this.instanceIdGeneratorType);
        }
        if (this.jobStoreType != null) {
            this.bind(JobStore.class).to(this.jobStoreType);
        }
        bind(this.binder(), this.schedulerPluginTypes, SchedulerPlugin.class, false);
        if (this.schedulerSignalerProviderType != null) {
            this.bind(SchedulerSignaler.class).toProvider(this.schedulerSignalerProviderType);
        }
        if (this.threadPoolProviderType != null) {
            this.bind(ThreadPool.class).toProvider(this.threadPoolProviderType);
        }
    }

}
