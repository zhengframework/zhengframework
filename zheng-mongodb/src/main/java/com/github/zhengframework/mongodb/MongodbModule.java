package com.github.zhengframework.mongodb;

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
    Map<String, MongodbConfig> mongodbConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), MongodbConfig.class);

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
