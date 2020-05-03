package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.Every;

@Every("10ms")
public class EveryTestJob extends AbstractJob {

    public EveryTestJob() {
        super(5);
    }
}
