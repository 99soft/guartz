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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.internal.Stopwatch;

/**
 * 
 * @version $Id$
 */
public class Quartz4GuiceTimerTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private TimedTask timedTask;

    @Inject
    private Scheduler scheduler;

    public void setTimedTask(TimedTask timedTask) {
        this.timedTask = timedTask;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(new SchedulerModule().addJob(TimedTask.class));
        injector.injectMembers(this);
    }

    @After
    public void tearDown() throws Exception {
        this.scheduler.shutdown();
    }

    @Test
    public void minimalTest() throws InterruptedException {
        this.logger.info("Timer test starting");

        Stopwatch stopwatch = new Stopwatch();
        Thread.sleep(5000);
        Assert.assertTrue(this.timedTask.getInvocationsTimedTaskA() > 0);

        this.logger.info("Done checking task A {} ms ", stopwatch.reset());
    }

}
