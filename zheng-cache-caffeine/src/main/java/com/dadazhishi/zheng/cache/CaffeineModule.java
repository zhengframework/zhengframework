package com.github.zhengframework.cache;

import com.github.benmanes.caffeine.jcache.spi.CaffeineCachingProvider;
import com.google.inject.AbstractModule;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.spi.CachingProvider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public abstract class CaffeineModule extends AbstractModule {

  @Override
  protected void configure() {
    CachingProvider cachingProvider =
        Caching.getCachingProvider(CaffeineCachingProvider.class.getName());
    CacheManager cacheManager = cachingProvider
        .getCacheManager(cachingProvider.getDefaultURI(), cachingProvider.getDefaultClassLoader());
    configureCacheManager(cacheManager);
    bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
    bind(CacheManager.class).toInstance(cacheManager);
  }

  protected abstract void configureCacheManager(CacheManager cacheManager);
}
