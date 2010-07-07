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

import java.util.TimeZone;

import org.quartz.CronTrigger;
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

    private final static String DEFAULT = "##default";

    @Inject
    private Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * {@inheritDoc}
     */
    public <T> void hear(TypeLiteral<T> typeLiteral,
            TypeEncounter<T> typeEncounter) {
        Class<?> jobClass = typeLiteral.getRawType();

        Scheduled scheduled = jobClass.getAnnotation(Scheduled.class);

        JobDetail jobDetail = new JobDetail(scheduled.jobName(), // job name
                scheduled.jobGroup(), // job group (you can also specify 'null'
                                   // to use the default group)
                jobClass, // the java class to execute
                scheduled.volatility(),
                scheduled.durability(),
                scheduled.recover());

        for (String jobListenerName : scheduled.jobListenerNames()) {
            jobDetail.addJobListener(jobListenerName);
        }

        String triggerName = null;
        if (DEFAULT.equals(scheduled.triggerName())) {
            triggerName = jobClass.getCanonicalName();
        } else {
            triggerName = scheduled.triggerName();
        }

        TimeZone timeZone = null;
        if (DEFAULT.equals(scheduled.timeZoneId())) {
            timeZone = TimeZone.getDefault();
        } else {
            timeZone = TimeZone.getTimeZone(scheduled.timeZoneId());
            if (timeZone == null) {
                timeZone = TimeZone.getDefault();
            }
        }

        try {
            CronTrigger trigger = new CronTrigger(triggerName,
                    scheduled.triggerGroup(),
                    scheduled.jobName(),
                    scheduled.jobGroup(),
                    scheduled.cronExpression(),
                    timeZone);
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
