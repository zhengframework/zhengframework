package com.github.zhengframework.job.jobs;

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

import com.github.zhengframework.job.AbstractJob;
import com.github.zhengframework.job.annotations.Every;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

// this job has the same job name as EveryTestJobWithJobName. Only one will be created
@Every(value = "1s", jobName = "FooJob")
public class EveryTestJobWithSameJobName extends AbstractJob {

  public static List<String> results = new ArrayList<>();

  @Override
  public void doJob(JobExecutionContext context) throws JobExecutionException {
    results.add(new Date().toString());
  }
}
