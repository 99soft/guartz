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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import com.google.inject.Inject;
import com.google.inject.Singleton;

public class GuartzSimpleTriggerTimerTestCase {

  @Inject
  private SimpleTask timedTask;

  @Inject
  private Scheduler scheduler;

  @Before
  public void setUp()
      throws Exception
  {
      createInjector( new QuartzModule()
      {

          @Override
          protected void schedule()
          {
              Trigger trigger = TriggerBuilder
                  .newTrigger()
                  .withSchedule( SimpleScheduleBuilder.repeatSecondlyForever() )
                  .build();
              
              scheduleJob( SimpleTask.class ).withTrigger( trigger );
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
      assertTrue( this.timedTask.getInvocation() > 0 );
  }
  
  @Singleton
  private static class SimpleTask implements Job 
  {

      private int invocation = 0;
    
      public void execute( JobExecutionContext context ) 
          throws JobExecutionException {
       invocation++;   
      }
      
      public int getInvocation() {
        return invocation;
      }
    
  }

  
}
