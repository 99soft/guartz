package org.slurry.quartz4guice.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.quartz.Scheduler;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scheduled {

    String name() default "myJob";

    String group() default Scheduler.DEFAULT_GROUP;

    boolean volatility() default false;

    boolean durability() default false;

    boolean recover() default false;

    String cronExpression();

}
