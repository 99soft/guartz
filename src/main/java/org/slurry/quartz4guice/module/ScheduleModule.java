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

import org.quartz.SchedulerFactory;
import org.slurry.quartz4guice.aop.ScheduledTypeListener;
import org.slurry.quartz4guice.scheduling.ScheduleFactoryProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.matcher.Matchers;

/**
 * 
 * @version $Id$
 */
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
