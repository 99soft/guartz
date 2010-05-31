package org.slurry.quartz4guice;

import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(InterfaceContainingTimedTask.class).to(TimedTasks.class);

	}

}
