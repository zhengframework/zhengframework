package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.Job;
import com.github.zhengframework.job.annotations.Every;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

// this job has the same job name as EveryTestJobWithJobName. Only one will be created
@Every(value = "1s", jobName = "FooJob")
public class EveryTestJobWithSameJobName extends Job {

    public static List<String> results = Lists.newArrayList();

    @Override
    public void doJob(JobExecutionContext context) throws JobExecutionException {
        results.add(new Date().toString());
    }
}
