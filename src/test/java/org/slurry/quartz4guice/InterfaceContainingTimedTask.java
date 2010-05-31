package org.slurry.quartz4guice;

public interface InterfaceContainingTimedTask {

	public void timedTaskA();

	public void timedTaskB();

	/**
	 * 
	 * @return the times task a has been invoked
	 */
	public int getInvocationsTimedTaskA();

	/**
	 * 
	 * @return the times task b has been invoked
	 */
	public int getInvocationsTimedTaskB();
}