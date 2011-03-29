/*
 *    Copyright 2009-2011 The 99 Software Foundation
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.nnsoft.guice.guartz.core;

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
