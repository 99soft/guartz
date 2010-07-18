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
package org.slurry.quartz4guice;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerListener;
import org.quartz.core.QuartzScheduler;
import org.quartz.core.QuartzSchedulerResources;
import org.quartz.core.SchedulingContext;
import org.quartz.spi.JobFactory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * 
 * @version $Id$
 */
@Singleton
final class QuartzSchedulerProvider implements Provider<QuartzScheduler> {

    private final QuartzScheduler scheduler;

    @Inject
    public QuartzSchedulerProvider(QuartzSchedulerResources resources,
            SchedulingContext ctxt,
            long idleWaitTime,
            long dbRetryInterval) throws SchedulerException {
        this.scheduler = new QuartzScheduler(resources, ctxt, idleWaitTime, dbRetryInterval);
        this.scheduler.start();
    }

    @Inject(optional = true)
    public void setJobFactory(JobFactory jobFactory) throws SchedulerException {
        this.scheduler.setJobFactory(jobFactory);
    }

    @Inject(optional = true)
    public void addGlobalJobListeners(@Global Set<JobListener> jobListeners) throws SchedulerException {
        for (JobListener jobListener : jobListeners) {
            this.scheduler.addGlobalJobListener(jobListener);
        }
    }

    @Inject(optional = true)
    public void addJobListeners(Set<JobListener> jobListeners) throws SchedulerException {
        for (JobListener jobListener : jobListeners) {
            this.scheduler.addJobListener(jobListener);
        }
    }

    @Inject(optional = true)
    public void addSchedulerListeners(Set<SchedulerListener> schedulerListeners) throws SchedulerException {
        for (SchedulerListener schedulerListener : schedulerListeners) {
            this.scheduler.addSchedulerListener(schedulerListener);
        }
    }

    @Inject(optional = true)
    public void addGlobalTriggerListeners(@Global Set<TriggerListener> triggerListeners) throws SchedulerException {
        for (TriggerListener triggerListener : triggerListeners) {
            this.scheduler.addGlobalTriggerListener(triggerListener);
        }
    }

    @Inject(optional = true)
    public void addTriggerListeners(Set<TriggerListener> triggerListeners) throws SchedulerException {
        for (TriggerListener triggerListener : triggerListeners) {
            this.scheduler.addTriggerListener(triggerListener);
        }
    }

    @Inject
    public void addJobs(Map<JobDetail, Trigger> jobs) throws SchedulerException {
        for (Entry<JobDetail, Trigger> job : jobs.entrySet()) {
            this.scheduler.scheduleJob(new SchedulingContext(), job.getKey(), job.getValue());
        }
    }

    public QuartzScheduler get() {
        return this.scheduler;
    }

}
