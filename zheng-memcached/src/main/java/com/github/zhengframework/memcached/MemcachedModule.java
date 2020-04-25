package com.github.zhengframework.memcached;

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
    Map<String, MemcachedConfig> memcachedConfigMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), MemcachedConfig.class);
    for (Entry<String, MemcachedConfig> entry : memcachedConfigMap
        .entrySet()) {
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
