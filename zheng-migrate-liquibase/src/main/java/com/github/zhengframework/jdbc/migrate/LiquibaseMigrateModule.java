package com.github.zhengframework.jdbc.migrate;

/*-
 * #%L
 * zheng-migrate-liquibase
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
import com.github.zhengframework.jdbc.ManagedSchema;
import com.google.inject.Key;
import com.google.inject.multibindings.OptionalBinder;
import java.util.Map;
import java.util.Map.Entry;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class LiquibaseMigrateModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, LiquibaseConfig> liquibaseConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), LiquibaseConfig.class);
    for (Entry<String, LiquibaseConfig> entry : liquibaseConfigMap.entrySet()) {
      String name = entry.getKey();
      LiquibaseConfig liquibaseConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(LiquibaseConfig.class).toInstance(liquibaseConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class))
            .setBinding()
            .toInstance(new LiquibaseManagedSchema(liquibaseConfig));
      } else {
        bind(Key.get(LiquibaseConfig.class, named(name))).toInstance(liquibaseConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class, named(name)))
            .setBinding()
            .toInstance(new LiquibaseManagedSchema(liquibaseConfig));
      }
    }
  }
}
