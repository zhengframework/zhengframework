package com.github.zhengframework.job.jobs;

import com.github.zhengframework.job.annotations.OnApplicationStop;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@OnApplicationStop
public class ApplicationStopTestJob extends AbstractJob {

    public ApplicationStopTestJob() {
        super(1);
    }

    @Override
    public void doJob(JobExecutionContext context) throws JobExecutionException {
        super.doJob(context);
    }
}
