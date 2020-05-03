package com.github.zhengframework.job;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;

public class JobModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, JobConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), JobConfig.class);
    JobConfig jobConfig = configMap.get("");
    bind(JobConfig.class).toInstance(jobConfig);
    bind(GuiceJobFactory.class);
    OptionalBinder.newOptionalBinder(binder(), SchedulerFactory.class)
        .setDefault().toProvider(SchedulerFactoryProvider.class);
    bind(Scheduler.class).toProvider(SchedulerProvider.class);
    bind(JobManagerService.class).asEagerSingleton();
  }
}
