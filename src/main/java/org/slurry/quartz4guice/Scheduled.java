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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.quartz.Scheduler;

/**
 * 
 * @version $Id$
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scheduled {

    public static final String DEFAULT = "##default";

    String jobName();

    String jobGroup() default Scheduler.DEFAULT_GROUP;

    String[] jobListenerNames() default {};

    boolean volatility() default false;

    boolean durability() default false;

    boolean recover() default false;

    String triggerName() default DEFAULT;

    String triggerGroup() default Scheduler.DEFAULT_MANUAL_TRIGGERS;

    String cronExpression();

    String timeZoneId() default DEFAULT;

    String schedulingContextId() default DEFAULT;

}
