package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.spi.JdbiPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

@EqualsAndHashCode(callSuper = false)
public class JdbiModule extends ConfigurationAwareModule {


  @Override
  protected void configure() {
    Map<String, JdbiConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), JdbiConfig.class);
    for (Entry<String, JdbiConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      JdbiConfig jdbiConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(JdbiConfig.class).toInstance(jdbiConfig);

        if (jdbiConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(new TypeLiteral<List<JdbiPlugin>>() {
          }))
              .setDefault().toInstance(Collections.singletonList(new SqlObjectPlugin()));
          bind(Jdbi.class).toProvider(new JdbiProvider(
              getProvider(Key.get(DataSource.class)),
              getProvider(Key.get(new TypeLiteral<List<JdbiPlugin>>() {
              }))));
        }
      } else {
        bind(Key.get(JdbiConfig.class, named(name))).toInstance(jdbiConfig);
        if (jdbiConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(new TypeLiteral<List<JdbiPlugin>>() {
          }, named(name)))
              .setDefault().toInstance(Collections.singletonList(new SqlObjectPlugin()));
          bind(Key.get(Jdbi.class, named(name))).toProvider(new JdbiProvider(
              getProvider(Key.get(DataSource.class, named(name))),
              getProvider(Key.get(new TypeLiteral<List<JdbiPlugin>>() {
              }, named(name)))));
        }
      }
    }
  }

}
