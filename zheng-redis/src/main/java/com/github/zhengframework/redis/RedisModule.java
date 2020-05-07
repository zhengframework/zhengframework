package com.github.zhengframework.redis;

/*-
 * #%L
 * zheng-redis
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
import io.lettuce.core.RedisClient;
import java.util.Map;
import java.util.Map.Entry;

public class RedisModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, RedisConfig> redisConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), RedisConfig.class);
    for (Entry<String, RedisConfig> entry : redisConfigMap.entrySet()) {
      if (entry.getKey().isEmpty()) {
        RedisConfig redisConfig = entry.getValue();
        bind(RedisConfig.class).toInstance(redisConfig);
        bind(RedisClient.class).toProvider(RedisClientProvider.class);
      } else {
        String name = entry.getKey();
        RedisConfig redisConfig = entry.getValue();
        bind(Key.get(RedisConfig.class, named(name))).toInstance(redisConfig);
        bind(Key.get(RedisClient.class, named(name)))
            .toProvider(new RedisClientProvider(redisConfig));
      }
    }
  }
}
