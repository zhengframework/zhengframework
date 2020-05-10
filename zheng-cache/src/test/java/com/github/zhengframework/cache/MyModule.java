package com.github.zhengframework.cache;

import com.github.zhengframework.core.Configurer;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.CacheManager;
import org.cache2k.Cache2kBuilder;
import org.cache2k.jcache.ExtendedMutableConfiguration;

public class MyModule extends AbstractModule {
  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(
        binder(), new TypeLiteral<Configurer<CacheManager>>() {})
        .setBinding()
        .toInstance(
            cacheManager -> {
              String cacheName = "guice";
              Cache<?, ?> cache = cacheManager.getCache(cacheName);
              if (cache == null) {
                cacheManager.createCache(
                    "guice",
                    ExtendedMutableConfiguration.of(
                        new Cache2kBuilder<Integer, Integer>() {}.entryCapacity(1000)
                            .expireAfterWrite(10, TimeUnit.SECONDS)));
              }
            });
  }
}
