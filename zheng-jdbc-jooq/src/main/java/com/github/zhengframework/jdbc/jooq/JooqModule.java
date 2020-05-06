package com.github.zhengframework.jdbc.jooq;

/*-
 * #%L
 * zheng-jdbc-jooq
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
    Map<String, JooqConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), JooqConfig.class);
    for (Entry<String, JooqConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      JooqConfig jooqConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(JooqConfig.class).toInstance(jooqConfig);
        if (jooqConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(SQLDialect.class))
              .setDefault()
              .toInstance(SQLDialect.DEFAULT);
          bind(Key.get(DSLContext.class))
              .toProvider(
                  new DSLContextProvider(
                      getProvider(Key.get(DataSource.class)),
                      getProvider(Key.get(SQLDialect.class)),
                      getProvider(Key.get(Settings.class))));
        }
      } else {
        bind(Key.get(JooqConfig.class, named(name))).toInstance(jooqConfig);
        if (jooqConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(SQLDialect.class, named(name)))
              .setDefault()
              .toInstance(SQLDialect.DEFAULT);
          bind(Key.get(DSLContext.class, named(name)))
              .toProvider(
                  new DSLContextProvider(
                      getProvider(Key.get(DataSource.class, named(name))),
                      getProvider(Key.get(SQLDialect.class, named(name))),
                      getProvider(Key.get(Settings.class))));
        }
      }
    }
  }
}
