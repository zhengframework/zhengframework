package com.github.zhengframework.healthcheck;

import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.health.jvm.ThreadDeadlockHealthCheck;
import com.github.zhengframework.healthcheck.datasource.DataSourceHealthCheck;
import com.github.zhengframework.healthcheck.sys.MemoryStatusHealthCheck;
import com.github.zhengframework.healthcheck.sys.SystemLoadHealthCheck;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.OptionalBinder;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class HealthCheckModule extends AbstractModule {

  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(binder(), HealthCheckRegistry.class)
        .setDefault().toInstance(new HealthCheckRegistry());
    bind(HealthCheckManagedService.class).asEagerSingleton();
    bind(SystemLoadHealthCheck.class);
    bind(ThreadDeadlockHealthCheck.class);
    bind(MemoryStatusHealthCheck.class);
    bind(DataSourceHealthCheck.class);
  }

}
