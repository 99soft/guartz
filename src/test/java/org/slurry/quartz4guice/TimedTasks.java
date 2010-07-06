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

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slurry.quartz4guice.annotation.Scheduled;

/**
 * 
 * @version $Id$
 */
/*0/15*/
@Scheduled(jobName = "test", cronExpression = "0/3 * * * * ?")
public class TimedTasks implements InterfaceContainingTimedTask, Job {

    private static Integer invocationsA = 0;

    public void timedTaskA() {
        invocationsA++;
    }

    public int getInvocationsTimedTaskA() {
        return invocationsA;
    }

    public void execute(JobExecutionContext context)
            throws JobExecutionException {
        timedTaskA();
    }

}
