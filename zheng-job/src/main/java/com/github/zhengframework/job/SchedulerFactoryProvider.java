package com.github.zhengframework.job;

import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.Properties;
import javax.inject.Singleton;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

@Singleton
public class SchedulerFactoryProvider implements Provider<SchedulerFactory> {

  private final JobConfig jobConfig;

  @Inject
  public SchedulerFactoryProvider(JobConfig jobConfig) {
    this.jobConfig = jobConfig;
  }

  @Override
  public SchedulerFactory get() {
    StdSchedulerFactory factory = new StdSchedulerFactory();
    Properties properties = new Properties();
    properties.putAll(jobConfig.getProperties());
    try {
      factory.initialize(properties);
      return factory;
    } catch (SchedulerException e) {
      throw new RuntimeException(e);
    }
  }
}
