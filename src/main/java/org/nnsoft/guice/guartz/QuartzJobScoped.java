package org.nnsoft.guice.guartz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

/**
 * Apply this to implementation classes when you want one instance per quartz job.
 *
 * @author Bruno Medeiros
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface QuartzJobScoped 
{
	//nothing
}
