package com.github.zhengframework.jdbc.ebean;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import io.ebean.Database;
import javax.sql.DataSource;

public class EbeanModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {

    ConfigurationBeanMapper
        .resolve(getConfiguration(), EbeanConfig.class, (name, ebeanConfig) -> {
          if (name.isEmpty()) {
            OptionalBinder.newOptionalBinder(binder(), Key.get(DatabaseConfigConfigurer.class))
                .setDefault().toInstance(
                databaseConfig -> {
                });
            bind(Key.get(Database.class)).toProvider(new DataBaseProvider(ebeanConfig
                , getProvider(Key.get(DataSource.class))
                , getProvider(Key.get(DatabaseConfigConfigurer.class))));
          } else {
            ebeanConfig.setName(name);
            OptionalBinder
                .newOptionalBinder(binder(), Key.get(DatabaseConfigConfigurer.class, named(name)))
                .setDefault().toInstance(
                databaseConfig -> {
                });
            bind(Key.get(Database.class, named(name))).toProvider(new DataBaseProvider(ebeanConfig
                , getProvider(Key.get(DataSource.class, named(name)))
                , getProvider(Key.get(DatabaseConfigConfigurer.class, named(name)))));
          }
        });

  }
}
