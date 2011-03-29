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
package org.nnsoft.guice.guartz.scheduler;

import javax.inject.Provider;

import org.nnsoft.guice.guartz.AbstractGuartzModule;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

import com.google.inject.Scopes;

/**
 * 
 */
public abstract class SchedulerModule extends AbstractGuartzModule {

    private Class<? extends Provider<SchedulerFactory>> schedulerFactoryProviderClass = StdSchedulerFactoryProvider.class;

    protected final <SF extends Provider<SchedulerFactory>> void bindSchedulerFactoryToProviderClass(Class<SF> schedulerFactoryProviderClass) {
        this.schedulerFactoryProviderClass = schedulerFactoryProviderClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configureGuartz() {
        bind(SchedulerFactory.class).toProvider(this.schedulerFactoryProviderClass).in(Scopes.SINGLETON);
        bind(Scheduler.class).toProvider(SchedulerProvider.class).asEagerSingleton();

        configureScheduler();
    }

    /**
     * 
     */
    protected abstract void configureScheduler();

}
