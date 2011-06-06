package org.nnsoft.guice.guartz;

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

import static com.google.inject.Guice.createInjector;
import static junit.framework.Assert.assertTrue;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Scheduler;

/**
 * 
 */
public class GuartzTimerTestCase
{

    @Inject
    private TimedTask timedTask;

    @Inject
    private Scheduler scheduler;

    public void setTimedTask( TimedTask timedTask )
    {
        this.timedTask = timedTask;
    }

    public void setScheduler( Scheduler scheduler )
    {
        this.scheduler = scheduler;
    }

    @Before
    public void setUp()
        throws Exception
    {
        createInjector( new QuartzModule()
        {

            @Override
            protected void schedule()
            {
                scheduleJob( TimedTask.class );
            }

        } ).injectMembers( this );
    }

    @After
    public void tearDown()
        throws Exception
    {
        this.scheduler.shutdown();
    }

    @Test
    public void minimalTest()
        throws Exception
    {
        Thread.sleep( 5000 );
        assertTrue( this.timedTask.getInvocationsTimedTaskA() > 0 );
    }

}
