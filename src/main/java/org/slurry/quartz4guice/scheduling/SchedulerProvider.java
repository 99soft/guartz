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
package org.slurry.quartz4guice.scheduling;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.spi.JobFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * 
 * @version $Id$
 */
@Singleton
public final class SchedulerProvider implements Provider<Scheduler> {

    private final Scheduler scheduler;

    @Inject
    public SchedulerProvider(SchedulerFactory schedulerFactory) {
        try {
            this.scheduler = schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException("Impossible to create the Scheduler from the SchedulerFactory",
                    e);
        }
    }

    @Inject(optional = true)
    public void setJobFactory(JobFactory jobFactory) throws SchedulerException {
        this.scheduler.setJobFactory(jobFactory);
    }

    public Scheduler get() {
        try {
            if (!this.scheduler.isStarted()) {
                this.scheduler.start();
            }
        } catch (SchedulerException e) {
            throw new RuntimeException("Impossible to check if the Scheduler is started",
                    e);
        }
        return this.scheduler;
    }

}
