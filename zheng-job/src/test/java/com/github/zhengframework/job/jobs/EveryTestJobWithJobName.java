package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.Every;

@Every(value = "10ms", jobName = "FooJob")
public class EveryTestJobWithJobName extends AbstractJob {

    public EveryTestJobWithJobName() {
        super(5);
    }
}
