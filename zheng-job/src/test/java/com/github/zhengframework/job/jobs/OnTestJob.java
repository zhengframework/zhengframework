package com.github.zhengframework.job.jobs;


import com.github.zhengframework.job.annotations.On;

@On("0/1 * * * * ?")
public class OnTestJob extends AbstractJob {

    public OnTestJob() {
        super(1);
    }
}
