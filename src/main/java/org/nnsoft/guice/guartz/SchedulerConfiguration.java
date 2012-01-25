package org.nnsoft.guice.guartz;

/**
 * Configuration of scheduler. 
 */
class SchedulerConfiguration implements SchedulerConfigurationBuilder {

	private boolean manualStart = false;

	public SchedulerConfiguration withManualStart() {
		this.manualStart = true;
		return this;
	}
	
	boolean startManually() {
		return manualStart;
	}
}
