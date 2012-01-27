package org.nnsoft.guice.guartz;

/**
 * Contains methods to change scheduler configuration by subclasses of QuartzModule.
 */
public interface SchedulerConfigurationBuilder {
	SchedulerConfiguration withManualStart();
}
