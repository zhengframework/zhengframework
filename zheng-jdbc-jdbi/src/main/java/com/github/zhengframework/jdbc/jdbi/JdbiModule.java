package com.github.zhengframework.jdbc.jdbi;

/*-
 * #%L
 * zheng-jdbc-jdbi
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
    Map<String, JdbiConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), JdbiConfig.class);
    for (Entry<String, JdbiConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      JdbiConfig jdbiConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(JdbiConfig.class).toInstance(jdbiConfig);

        if (jdbiConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(
              binder(), Key.get(new TypeLiteral<List<JdbiPlugin>>() {
              }))
              .setDefault()
              .toInstance(Collections.singletonList(new SqlObjectPlugin()));
          bind(Jdbi.class)
              .toProvider(
                  new JdbiProvider(
                      getProvider(Key.get(DataSource.class)),
                      getProvider(Key.get(new TypeLiteral<List<JdbiPlugin>>() {
                      }))));
        }
      } else {
        bind(Key.get(JdbiConfig.class, named(name))).toInstance(jdbiConfig);
        if (jdbiConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(
              binder(), Key.get(new TypeLiteral<List<JdbiPlugin>>() {
              }, named(name)))
              .setDefault()
              .toInstance(Collections.singletonList(new SqlObjectPlugin()));
          bind(Key.get(Jdbi.class, named(name)))
              .toProvider(
                  new JdbiProvider(
                      getProvider(Key.get(DataSource.class, named(name))),
                      getProvider(Key.get(new TypeLiteral<List<JdbiPlugin>>() {
                      }, named(name)))));
        }
      }
    }
  }
}
