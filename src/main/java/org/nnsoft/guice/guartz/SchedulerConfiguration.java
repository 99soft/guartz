package org.nnsoft.guice.guartz;

class SchedulerConfiguration implements SchedulerConfigurationBuilder {

	private boolean manualStart;

	public SchedulerConfiguration withManualStart() {
		this.manualStart = true;
		return this;
	}
	
	boolean startManually() {
		return manualStart;
	}
}
