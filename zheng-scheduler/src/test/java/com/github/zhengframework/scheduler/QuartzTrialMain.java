package com.github.zhengframework.scheduler;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class QuartzTrialMain {

  public static void main(String[] args) throws Exception {
    Injector injector = Guice.createInjector(new QuartzModule());

    TrialQuartz trialQuartz = injector.getInstance(TrialQuartz.class);

    trialQuartz.run();
  }
}
