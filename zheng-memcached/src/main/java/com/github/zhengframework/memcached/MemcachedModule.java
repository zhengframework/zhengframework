package com.github.zhengframework.memcached;

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAware;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import java.util.Map;
import java.util.Map.Entry;
import net.rubyeye.xmemcached.MemcachedClient;

public class MemcachedModule extends AbstractModule implements ConfigurationAware {

  private Configuration configuration;

  @Override
  protected void configure() {
    Preconditions.checkArgument(configuration != null, "configuration is null");
    Boolean group = configuration.getBoolean(MemcachedConfig.PREFIX + ".group", false);
    if (!group) {
      MemcachedConfig memcachedConfig = ConfigurationBeanMapper
          .resolve(configuration, MemcachedConfig.PREFIX, MemcachedConfig.class);
      bind(MemcachedConfig.class).toInstance(memcachedConfig);
      bind(MemcachedClient.class).toProvider(MemcachedClientProvider.class);
    } else {
      Map<String, MemcachedConfig> map = ConfigurationBeanMapper
          .resolveMap(configuration, MemcachedConfig.PREFIX, MemcachedConfig.class);
      for (Entry<String, MemcachedConfig> entry : map.entrySet()) {
        String name = entry.getKey();
        MemcachedConfig memcachedConfig = entry.getValue();
        bind(Key.get(MemcachedConfig.class, named(name))).toInstance(memcachedConfig);
        bind(Key.get(MemcachedClient.class, named(name)))
            .toProvider(new MemcachedClientProvider(memcachedConfig));
      }
    }
  }

  @Override
  public void initConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
