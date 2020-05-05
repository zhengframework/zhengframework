package com.github.zhengframework.memcached;

/*-
 * #%L
 * zheng-memcached
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
import java.util.Map;
import java.util.Map.Entry;
import net.rubyeye.xmemcached.MemcachedClient;

public class MemcachedModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, MemcachedConfig> memcachedConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), MemcachedConfig.class);
    for (Entry<String, MemcachedConfig> entry : memcachedConfigMap.entrySet()) {
      if (entry.getKey().isEmpty()) {
        MemcachedConfig memcachedConfig = entry.getValue();
        bind(MemcachedConfig.class).toInstance(memcachedConfig);
        bind(MemcachedClient.class).toProvider(MemcachedClientProvider.class);
      } else {
        String name = entry.getKey();
        MemcachedConfig memcachedConfig = entry.getValue();
        bind(Key.get(MemcachedConfig.class, named(name))).toInstance(memcachedConfig);
        bind(Key.get(MemcachedClient.class, named(name)))
            .toProvider(new MemcachedClientProvider(memcachedConfig));
      }
    }
  }
}
