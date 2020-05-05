package com.github.zhengframework.jdbc.ebean;

/*-
 * #%L
 * zheng-jdbc-ebean
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
import io.ebean.Database;
import javax.sql.DataSource;

public class EbeanModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {

    ConfigurationBeanMapper.resolve(
        getConfiguration(),
        EbeanConfig.class,
        (name, ebeanConfig) -> {
          if (name.isEmpty()) {
            OptionalBinder.newOptionalBinder(binder(), Key.get(DatabaseConfigConfigurer.class))
                .setDefault()
                .toInstance(databaseConfig -> {
                });
            bind(Key.get(Database.class))
                .toProvider(
                    new DataBaseProvider(
                        ebeanConfig,
                        getProvider(Key.get(DataSource.class)),
                        getProvider(Key.get(DatabaseConfigConfigurer.class))));
          } else {
            ebeanConfig.setName(name);
            OptionalBinder.newOptionalBinder(
                binder(), Key.get(DatabaseConfigConfigurer.class, named(name)))
                .setDefault()
                .toInstance(databaseConfig -> {
                });
            bind(Key.get(Database.class, named(name)))
                .toProvider(
                    new DataBaseProvider(
                        ebeanConfig,
                        getProvider(Key.get(DataSource.class, named(name))),
                        getProvider(Key.get(DatabaseConfigConfigurer.class, named(name)))));
          }
        });
  }
}
