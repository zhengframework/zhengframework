package com.github.zhengframework.jdbc;

/*-
 * #%L
 * zheng-jdbc
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
import com.github.zhengframework.jdbc.wrapper.DataSourceWrapper;
import com.github.zhengframework.jdbc.wrapper.DataSourceWrapperProvider;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;
import com.google.inject.name.Named;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.sql.DataSource;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class DataSourceModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, DataSourceConfig> dataSourceConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), DataSourceConfig.class);
    for (Entry<String, DataSourceConfig> entry : dataSourceConfigMap.entrySet()) {
      String name = entry.getKey();
      DataSourceConfig dataSourceConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(DataSourceConfig.class).toInstance(dataSourceConfig);
        bind(DataSourceWrapper.class)
            .toProvider(
                new DataSourceWrapperProvider(
                    getProvider(DataSourceConfig.class), getProvider(Injector.class)));
        OptionalBinder.newOptionalBinder(binder(), DataSource.class)
            .setDefault()
            .toProvider(
                new DataSourceProvider(
                    getProvider(Key.get(DataSourceWrapper.class)),
                    getProvider(Key.get(new TypeLiteral<Set<DataSourceProxy>>() {})),
                    getProvider(Key.get(ManagedSchema.class))));

        bind(DataSourceService.class).asEagerSingleton();

        Multibinder.newSetBinder(binder(), DataSourceProxy.class)
            .addBinding()
            .toInstance(dataSource -> dataSource);
        OptionalBinder.newOptionalBinder(binder(), ManagedSchema.class)
            .setDefault()
            .toInstance(dataSource -> {});
      } else {
        Named annotation = named(name);
        bind(Key.get(DataSourceConfig.class, annotation)).toInstance(dataSourceConfig);

        bind(Key.get(DataSourceWrapper.class, annotation))
            .toProvider(
                new DataSourceWrapperProvider(
                    getProvider(Key.get(DataSourceConfig.class, annotation)),
                    getProvider(Injector.class)));
        OptionalBinder.newOptionalBinder(binder(), Key.get(DataSource.class, annotation))
            .setDefault()
            .toProvider(
                new DataSourceProvider(
                    getProvider(Key.get(DataSourceWrapper.class, annotation)),
                    getProvider(Key.get(new TypeLiteral<Set<DataSourceProxy>>() {}, annotation)),
                    getProvider(Key.get(ManagedSchema.class, annotation))));

        bind(Key.get(DataSourceService.class, annotation))
            .toInstance(
                new DataSourceService(getProvider(Key.get(DataSourceWrapper.class, annotation))));

        Multibinder.newSetBinder(binder(), Key.get(DataSourceProxy.class, annotation))
            .addBinding()
            .toInstance(dataSource -> dataSource);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class, annotation))
            .setDefault()
            .toInstance(dataSource -> {});
      }
    }
  }
}
