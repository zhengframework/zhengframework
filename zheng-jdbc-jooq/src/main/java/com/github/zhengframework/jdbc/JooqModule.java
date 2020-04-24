package com.github.zhengframework.jdbc;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;

@EqualsAndHashCode(callSuper = false)
public class JooqModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, JooqConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), JooqConfig.class);
    for (Entry<String, JooqConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      JooqConfig jooqConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(JooqConfig.class).toInstance(jooqConfig);
        if (jooqConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(SQLDialect.class))
              .setDefault().toInstance(SQLDialect.DEFAULT);
          bind(Key.get(DSLContext.class))
              .toProvider(new DSLContextProvider(getProvider(Key.get(DataSource.class))
                  , getProvider(Key.get(SQLDialect.class))
                  , getProvider(Key.get(Settings.class))));
        }
      } else {
        bind(Key.get(JooqConfig.class, named(name))).toInstance(jooqConfig);
        if (jooqConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(SQLDialect.class, named(name)))
              .setDefault().toInstance(SQLDialect.DEFAULT);
          bind(Key.get(DSLContext.class, named(name)))
              .toProvider(new DSLContextProvider(getProvider(Key.get(DataSource.class, named(name)))
                  , getProvider(Key.get(SQLDialect.class, named(name)))
                  , getProvider(Key.get(Settings.class))));
        }
      }
    }
  }

}
