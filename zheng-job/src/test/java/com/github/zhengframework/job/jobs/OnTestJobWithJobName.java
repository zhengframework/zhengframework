package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.On;

@On(value = "0/1 * * * * ?", jobName = "BarJob")
public class OnTestJobWithJobName extends AbstractJob {

    public OnTestJobWithJobName() {
        super(1);
    }
}
