package com.github.zhengframework.scheduler;

import com.google.inject.ImplementedBy;

@ImplementedBy(TrialQuartzImpl.class)
public interface TrialQuartz {

  void run();
}
