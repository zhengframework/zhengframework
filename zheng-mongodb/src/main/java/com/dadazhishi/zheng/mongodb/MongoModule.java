package com.dadazhishi.zheng.mongodb;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.mongodb.client.MongoClient;
import java.util.Map;
import java.util.Map.Entry;

public class MongoModule extends AbstractModule implements ConfigurationSupport {

  private Configuration configuration;

  @Override
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configure() {
    Boolean group = configuration.getBoolean("zheng.mongo.group", false);
    if (!group) {
      MongoConfig mongoConfig = ConfigurationObjectMapper
          .resolve(configuration, MongoConfig.NAMESPACE, MongoConfig.class);
      bind(MongoConfig.class).toInstance(mongoConfig);
      bind(MongoClient.class).toProvider(MongoClientProvider.class);
    } else {
      Map<String, MongoConfig> map = ConfigurationObjectMapper
          .resolveMap(configuration, MongoConfig.NAMESPACE, MongoConfig.class);
      for (Entry<String, MongoConfig> entry : map.entrySet()) {
        String name = entry.getKey();
        MongoConfig mongoConfig = entry.getValue();
        bind(Key.get(MongoConfig.class, named(name))).toInstance(mongoConfig);
        bind(Key.get(MongoClient.class, named(name)))
            .toProvider(new MongoClientProvider(mongoConfig));
      }
    }
  }

}
