package com.github.zhengframework.mongodb;

/*-
 * #%L
 * zheng-mongodb
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
import com.mongodb.client.MongoClient;
import java.util.Map;
import java.util.Map.Entry;

public class MongodbModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, MongodbConfig> mongodbConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), MongodbConfig.class);

    for (Entry<String, MongodbConfig> entry : mongodbConfigMap.entrySet()) {
      if (entry.getKey().isEmpty()) {
        MongodbConfig mongodbConfig = entry.getValue();
        bind(MongodbConfig.class).toInstance(mongodbConfig);
        bind(MongoClient.class).toProvider(MongoClientProvider.class);
      } else {
        String name = entry.getKey();
        MongodbConfig mongodbConfig = entry.getValue();
        bind(Key.get(MongodbConfig.class, named(name))).toInstance(mongodbConfig);
        bind(Key.get(MongoClient.class, named(name)))
            .toProvider(new MongoClientProvider(mongodbConfig));
      }
    }
  }
}
