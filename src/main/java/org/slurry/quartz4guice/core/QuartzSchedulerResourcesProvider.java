package org.slurry.quartz4guice.core;

import java.util.Set;

import org.quartz.core.QuartzSchedulerResources;
import org.quartz.spi.JobStore;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.spi.ThreadPool;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
final class QuartzSchedulerResourcesProvider
        implements Provider<QuartzSchedulerResources> {

    private final QuartzSchedulerResources resources = new QuartzSchedulerResources();

    @Inject(optional = true)
    public void addSchedulerPlugin(Set<SchedulerPlugin> plugins) {
        for (SchedulerPlugin plugin : plugins) {
            this.resources.addSchedulerPlugin(plugin);
        }
    }

    @Inject(optional = true)
    public void setJobStore(JobStore jobStore) {
        this.resources.setJobStore(jobStore);
    }

    @Inject(optional = true)
    public void setThreadPool(ThreadPool threadPool) {
        this.resources.setThreadPool(threadPool);
    }

    public QuartzSchedulerResources get() {
        return this.resources;
    }

}
