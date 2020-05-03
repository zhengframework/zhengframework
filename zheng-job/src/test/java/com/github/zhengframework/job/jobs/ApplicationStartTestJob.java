package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.OnApplicationStart;

@OnApplicationStart
public class ApplicationStartTestJob extends AbstractJob {

    public ApplicationStartTestJob() {
        super(1);
    }
}
