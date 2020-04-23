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
public class LiquibaseMigrateModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    Map<String, LiquibaseConfig> liquibaseConfigMap = ConfigurationBeanMapper
        .resolve(configuration, LiquibaseConfig.class);
    for (Entry<String, LiquibaseConfig> entry : liquibaseConfigMap.entrySet()) {
      String name = entry.getKey();
      LiquibaseConfig liquibaseConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(LiquibaseConfig.class).toInstance(liquibaseConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class))
            .setBinding().toInstance(new LiquibaseManagedSchema(liquibaseConfig));
      } else {
        bind(Key.get(LiquibaseConfig.class, named(name))).toInstance(liquibaseConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class, named(name)))
            .setBinding().toInstance(new LiquibaseManagedSchema(liquibaseConfig));
      }
    }
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
