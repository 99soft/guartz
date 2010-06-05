package org.slurry.quartz4guice.aop;

import java.util.Date;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class ScheduledTypeListener implements TypeListener {



	public <T> void hear(TypeLiteral<T> typeLiteral, TypeEncounter<T> typeEncounter) {
	      for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
	        if (field.getType() == Logger.class
	            && field.isAnnotationPresent(InjectLogger.class)) {
	          typeEncounter.register(new Log4JMembersInjector<T>(field));
	        }
	      }
	    }


}
