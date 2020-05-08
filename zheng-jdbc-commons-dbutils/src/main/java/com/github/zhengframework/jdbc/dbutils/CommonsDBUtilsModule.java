package com.github.zhengframework.jdbc.dbutils;

/*-
 * #%L
 * zheng-jdbc-commons-dbutils
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import org.apache.commons.dbutils.AsyncQueryRunner;
import org.apache.commons.dbutils.QueryRunner;

@EqualsAndHashCode(callSuper = false)
public class CommonsDBUtilsModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, CommonsDbUtilsConfig> configMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), CommonsDbUtilsConfig.class);
    for (Entry<String, CommonsDbUtilsConfig> entry : configMap.entrySet()) {
      String name = entry.getKey();
      CommonsDbUtilsConfig dbUtilsConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(CommonsDbUtilsConfig.class).toInstance(dbUtilsConfig);
        if (dbUtilsConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(ExecutorService.class))
              .setDefault()
              .toInstance(Executors.newCachedThreadPool());
          bind(Key.get(QueryRunner.class))
              .toProvider(new QueryRunnerProvider(getProvider(Key.get(DataSource.class))));
          bind(Key.get(AsyncQueryRunner.class))
              .toProvider(
                  new AsyncQueryRunnerProvider(
                      getProvider(Key.get(DataSource.class)),
                      getProvider(Key.get(ExecutorService.class))));
        }
      } else {
        bind(Key.get(CommonsDbUtilsConfig.class, named(name))).toInstance(dbUtilsConfig);
        if (dbUtilsConfig.isEnable()) {
          OptionalBinder.newOptionalBinder(binder(), Key.get(ExecutorService.class, named(name)))
              .setDefault()
              .toInstance(Executors.newCachedThreadPool());
          bind(Key.get(QueryRunner.class, named(name)))
              .toProvider(
                  new QueryRunnerProvider(getProvider(Key.get(DataSource.class, named(name)))));
          bind(Key.get(AsyncQueryRunner.class, named(name)))
              .toProvider(
                  new AsyncQueryRunnerProvider(
                      getProvider(Key.get(DataSource.class, named(name))),
                      getProvider(Key.get(ExecutorService.class, named(name)))));
        }
      }
    }
  }
}
