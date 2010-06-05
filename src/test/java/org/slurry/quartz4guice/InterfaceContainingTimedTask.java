package org.slurry.quartz4guice;

public interface InterfaceContainingTimedTask {

	public void timedTaskA();

	/**
	 * 
	 * @return the times task a has been invoked
	 */
	public int getInvocationsTimedTaskA();

}