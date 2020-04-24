package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.Key;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import org.sql2o.Sql2o;

/**
 * https://github.com/aaberg/sql2o
 */
@EqualsAndHashCode(callSuper = false)
public class Sql2oModule extends ConfigurationAwareModule {


  @Override
  protected void configure() {
    Map<String, Sql2oConfig> sql2oConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), Sql2oConfig.class);
    for (Entry<String, Sql2oConfig> entry : sql2oConfigMap.entrySet()) {
      String name = entry.getKey();
      Sql2oConfig sql2oConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(Sql2oConfig.class).toInstance(sql2oConfig);
        if (sql2oConfig.isEnable()) {
          bind(Sql2o.class).toProvider(new Sql2oProvider(
              getProvider(DataSource.class))).in(Singleton.class);
        }
      } else {
        bind(Key.get(Sql2oConfig.class, named(name))).toInstance(sql2oConfig);
        if (sql2oConfig.isEnable()) {
          bind(Key.get(Sql2o.class, named(name))).toProvider(new Sql2oProvider(
              getProvider(Key.get(DataSource.class, named(name))))).in(Singleton.class);
        }
      }
    }
  }

}
