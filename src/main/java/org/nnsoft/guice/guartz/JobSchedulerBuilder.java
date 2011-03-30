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

import static java.util.TimeZone.getDefault;
import static org.nnsoft.guice.guartz.Scheduled.DEFAULT;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.utils.Key.DEFAULT_GROUP;

import java.util.TimeZone;

import javax.inject.Inject;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

/**
 * 
 */
public final class JobSchedulerBuilder {

    private final Class<? extends Job> jobClass;

    private String jobName;

    private String jobGroup = DEFAULT_GROUP;

    private boolean requestRecovery = false;

    private boolean storeDurably = false;

    private String triggerName = DEFAULT;

    private String triggerGroup = DEFAULT_GROUP;

    private String cronExpression;

    private TimeZone timeZone = getDefault();

    private int priority = 0;

    /**
     * 
     * This class can't be instantiated by users.
     */
    JobSchedulerBuilder(final Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public JobSchedulerBuilder withJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public JobSchedulerBuilder withJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
        return this;
    }

    public JobSchedulerBuilder withRequestRecovery(boolean requestRecovery) {
        this.requestRecovery = requestRecovery;
        return this;
    }

    public JobSchedulerBuilder withStoreDurably(boolean storeDurably) {
        this.storeDurably = storeDurably;
        return this;
    }

    public JobSchedulerBuilder withTriggerName(String triggerName) {
        this.triggerName = triggerName;
        return this;
    }

    public JobSchedulerBuilder withTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
        return this;
    }

    public JobSchedulerBuilder withCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
        return this;
    }

    public JobSchedulerBuilder withTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public JobSchedulerBuilder withPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Users MUST NOT use this method!
     *
     * @param scheduler
     * @throws Exception 
     */
    @Inject
    public void schedule(Scheduler scheduler) throws Exception {
        JobDetail jobDetail = newJob(jobClass)
            .withIdentity(jobName, jobGroup)
            .requestRecovery(requestRecovery)
            .storeDurably(storeDurably)
            .build();

        Trigger trigger = newTrigger()
            .withIdentity(triggerName, triggerGroup)
            .withSchedule(cronSchedule(cronExpression).inTimeZone(timeZone))
            .withPriority(priority)
            .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

}
