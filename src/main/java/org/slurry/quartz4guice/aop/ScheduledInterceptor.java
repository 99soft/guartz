package org.slurry.quartz4guice.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class ScheduledInterceptor implements MethodInterceptor {


	public Object invoke(MethodInvocation invocation) throws Throwable {

		return invocation.proceed();

	}

	

}
