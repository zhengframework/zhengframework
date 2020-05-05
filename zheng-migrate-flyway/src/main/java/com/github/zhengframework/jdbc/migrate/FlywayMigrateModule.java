package com.github.zhengframework.jdbc.migrate;

/*-
 * #%L
 * zheng-migrate-flyway
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
public class FlywayMigrateModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, FlywayConfig> flywayConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), FlywayConfig.class);
    for (Entry<String, FlywayConfig> entry : flywayConfigMap.entrySet()) {
      String name = entry.getKey();
      FlywayConfig flywayConfig = entry.getValue();
      if (name.isEmpty()) {
        bind(FlywayConfig.class).toInstance(flywayConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class))
            .setBinding()
            .toInstance(new FlywayManagedSchema(flywayConfig));
      } else {
        bind(Key.get(FlywayConfig.class, named(name))).toInstance(flywayConfig);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ManagedSchema.class, named(name)))
            .setBinding()
            .toInstance(new FlywayManagedSchema(flywayConfig));
      }
    }
  }
}
