package com.dadazhishi.zheng.memcached;

import static com.google.inject.name.Names.named;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationObjectMapper;
import com.dadazhishi.zheng.configuration.ConfigurationSupport;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import java.util.Map;
import java.util.Map.Entry;
import net.rubyeye.xmemcached.MemcachedClient;

public class MemcachedModule extends AbstractModule implements ConfigurationSupport {

  private Configuration configuration;

  @Override
  protected void configure() {
    Boolean group = configuration.getBoolean(MemcachedConfig.NAMESPACE + ".group", false);
    if (!group) {
      MemcachedConfig memcachedConfig = ConfigurationObjectMapper
          .resolve(configuration, MemcachedConfig.NAMESPACE, MemcachedConfig.class);
      bind(MemcachedConfig.class).toInstance(memcachedConfig);
      bind(MemcachedClient.class).toProvider(MemcachedClientProvider.class);
    } else {
      Map<String, MemcachedConfig> map = ConfigurationObjectMapper
          .resolveMap(configuration, MemcachedConfig.NAMESPACE, MemcachedConfig.class);
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
  public void setConfiguration(Configuration configuration) {
    this.configuration = configuration;
  }
}
