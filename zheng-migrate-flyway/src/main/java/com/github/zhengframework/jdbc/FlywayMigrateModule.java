package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class FlywayMigrateModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, FlywayConfig> metricsConfigMap = ConfigurationBeanMapper
        .resolve(configuration, FlywayConfig.class);
    for (Entry<String, FlywayConfig> entry : metricsConfigMap.entrySet()) {
      String name = entry.getKey();
      FlywayConfig flywayConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(FlywayConfig.class).toInstance(flywayConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class))
            .setBinding().toInstance(new FlywayManagedSchema(flywayConfig));
      } else {
        bind(Key.get(FlywayConfig.class, named(name))).toInstance(flywayConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class, named(name)))
            .setBinding().toInstance(new FlywayManagedSchema(flywayConfig));
      }

    }


  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}