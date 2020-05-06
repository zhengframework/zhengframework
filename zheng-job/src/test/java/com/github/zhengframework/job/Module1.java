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

import com.github.zhengframework.job.jobs.ApplicationStartTestJob;
import com.github.zhengframework.job.jobs.ApplicationStopTestJob;
import com.github.zhengframework.job.jobs.EveryTestJob;
import com.github.zhengframework.job.jobs.EveryTestJobWithDelay;
import com.github.zhengframework.job.jobs.EveryTestJobWithJobName;
import com.github.zhengframework.job.jobs.OnTestJob;
import com.github.zhengframework.job.jobs.OnTestJobWithJobName;
import com.google.inject.AbstractModule;

public class Module1 extends AbstractModule {

  @Override
  protected void configure() {
    bind(ApplicationStartTestJob.class).toInstance(JobModuleTest.startTestJob);
    bind(OnTestJob.class).toInstance(JobModuleTest.onTestJob);
    bind(OnTestJobWithJobName.class).toInstance(JobModuleTest.onTestJobWithJobName);
    bind(EveryTestJob.class).toInstance(JobModuleTest.everyTestJob);
    bind(EveryTestJobWithDelay.class).toInstance(JobModuleTest.everyTestJobWithDelay);
    bind(EveryTestJobWithJobName.class).toInstance(JobModuleTest.everyTestJobWithJobName);
    bind(ApplicationStopTestJob.class).toInstance(JobModuleTest.applicationStopTestJob);
  }
}
