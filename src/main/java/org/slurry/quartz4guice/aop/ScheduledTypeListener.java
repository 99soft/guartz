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
package org.slurry.quartz4guice.aop;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.slurry.quartz4guice.annotation.Scheduled;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * 
 * @version $Id$
 */
public final class ScheduledTypeListener implements TypeListener {

    @Inject
    private Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public <T> void hear(TypeLiteral<T> typeLiteral,
            TypeEncounter<T> typeEncounter) {
        if (typeLiteral.getRawType().isAnnotationPresent(Scheduled.class)) {
            startSchedule(typeLiteral.getRawType());
        }
    }

    private <T> void startSchedule(Class<T> jobClass) {
        if (!Job.class.isAssignableFrom(jobClass)) {
            throw new RuntimeException("Class '"
                    + jobClass.getName()
                    + "' is not a '"
                    + Job.class.getName()
                    + "' instance");
        }

        Scheduled scheduled = jobClass.getAnnotation(Scheduled.class);

        JobDetail jobDetail = new JobDetail(scheduled.jobName(), // job name
                scheduled.group(), // job group (you can also specify 'null'
                                   // to use the default group)
                jobClass, // the java class to execute
                scheduled.volatility(),
                scheduled.durability(),
                scheduled.recover());

        CronTrigger trigger = new CronTrigger(jobClass.getCanonicalName());

        try {
            trigger.setCronExpression(scheduled.cronExpression());
            this.scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while scheduling the Job '"
                    + jobDetail
                    + "' instance using cron expression '"
                    + scheduled.cronExpression()
                    + "'", e);
        }
    }

}
