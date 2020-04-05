package com.dadazhishi.zheng.mongodb;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.mongodb.client.MongoClient;
import java.util.Map;
import java.util.Map.Entry;

public class MongoModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    Boolean group = configuration.getBoolean("zheng.mongo.group", false);
    if (!group) {
      MongoConfig mongoConfig = ConfigurationBeanMapper
          .resolve(configuration, MongoConfig.PREFIX, MongoConfig.class);
      bind(MongoConfig.class).toInstance(mongoConfig);
      bind(MongoClient.class).toProvider(MongoClientProvider.class);
    } else {
      Map<String, MongoConfig> map = ConfigurationBeanMapper
          .resolveMap(configuration, MongoConfig.PREFIX, MongoConfig.class);
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
