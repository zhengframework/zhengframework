package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.DelayStart;
import com.github.zhengframework.job.annotations.Every;

@DelayStart("1s")
@Every("10ms")
public class EveryTestJobWithDelay extends AbstractJob {

    public EveryTestJobWithDelay() {
        super(5);
    }
}
