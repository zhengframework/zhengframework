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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

@EqualsAndHashCode(callSuper = false)
public class CommonsDBUtilsModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");

    Map<String, CommonsDbUtilsConfig> configMap = ConfigurationBeanMapper
        .resolve(configuration, CommonsDbUtilsConfig.class);
    for (Entry<String, CommonsDbUtilsConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      CommonsDbUtilsConfig dbUtilsConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(CommonsDbUtilsConfig.class).toInstance(dbUtilsConfig);
        if (dbUtilsConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(ExecutorService.class))
              .setDefault().toInstance(Executors.newCachedThreadPool());
          bind(Key.get(QueryRunner.class))
              .toProvider(new QueryRunnerProvider(getProvider(Key.get(DataSource.class))));
          bind(Key.get(AsyncQueryRunner.class))
              .toProvider(new AsyncQueryRunnerProvider(getProvider(Key.get(DataSource.class))
                  , getProvider(Key.get(ExecutorService.class))));
        }
      } else {
        bind(Key.get(CommonsDbUtilsConfig.class, named(name))).toInstance(dbUtilsConfig);
        if (dbUtilsConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(ExecutorService.class, named(name)))
              .setDefault().toInstance(Executors.newCachedThreadPool());
          bind(Key.get(QueryRunner.class, named(name)))
              .toProvider(
                  new QueryRunnerProvider(getProvider(Key.get(DataSource.class, named(name)))));
          bind(Key.get(AsyncQueryRunner.class, named(name)))
              .toProvider(
                  new AsyncQueryRunnerProvider(getProvider(Key.get(DataSource.class, named(name)))
                      , getProvider(Key.get(ExecutorService.class, named(name)))));
        }
      }
    }
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}