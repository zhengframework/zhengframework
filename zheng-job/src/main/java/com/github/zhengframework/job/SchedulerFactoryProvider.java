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

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Properties;
import javax.inject.Singleton;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

@Singleton
public class SchedulerFactoryProvider implements Provider<SchedulerFactory> {

  private final JobConfig jobConfig;

  @Inject
  public SchedulerFactoryProvider(JobConfig jobConfig) {
    this.jobConfig = jobConfig;
  }

  @Override
  public SchedulerFactory get() {
    StdSchedulerFactory factory = new StdSchedulerFactory();
    Properties properties = new Properties();
    properties.putAll(jobConfig.getProperties());
    try {
      factory.initialize(properties);
      return factory;
    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }
}
