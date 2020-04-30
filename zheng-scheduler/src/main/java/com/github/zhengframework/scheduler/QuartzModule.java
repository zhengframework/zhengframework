package com.github.zhengframework.scheduler;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    bind(SchedulerFactory.class).toInstance(new StdSchedulerFactory());
    bind(GuiceJobFactory.class);
    bind(QuartzEx.class).to(QuartzExImpl.class);
  }
}
