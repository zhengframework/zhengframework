package com.dadazhishi.zheng.redis;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationAware;
import com.dadazhishi.zheng.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import io.lettuce.core.RedisClient;
import java.util.Map;
import java.util.Map.Entry;

public class RedisModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    Boolean group = configuration.getBoolean("zheng.redis.group", false);
    if (!group) {
      RedisConfig redisConfig = ConfigurationBeanMapper
          .resolve(configuration, RedisConfig.PREFIX, RedisConfig.class);
      bind(RedisConfig.class).toInstance(redisConfig);
      bind(RedisClient.class).toProvider(RedisClientProvider.class);
    } else {
      Map<String, RedisConfig> map = ConfigurationBeanMapper
          .resolveMap(configuration, RedisConfig.PREFIX, RedisConfig.class);
      for (Entry<String, RedisConfig> entry : map.entrySet()) {
        String name = entry.getKey();
        RedisConfig redisConfig = entry.getValue();
        bind(Key.get(RedisConfig.class, named(name))).toInstance(redisConfig);
        bind(Key.get(RedisClient.class, named(name)))
            .toProvider(new RedisClientProvider(redisConfig));
      }
    }
  }
}
