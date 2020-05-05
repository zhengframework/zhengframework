package com.github.zhengframework.job;

/*-
 * #%L
 * zheng-job
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

public class JobModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, JobConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), JobConfig.class);
    JobConfig jobConfig = configMap.get("");
    bind(JobConfig.class).toInstance(jobConfig);
    bind(GuiceJobFactory.class);
    OptionalBinder.newOptionalBinder(binder(), SchedulerFactory.class)
        .setDefault()
        .toProvider(SchedulerFactoryProvider.class);
    bind(Scheduler.class).toProvider(SchedulerProvider.class);
    bind(JobManagerService.class).asEagerSingleton();
  }
}
