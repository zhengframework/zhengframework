package com.github.zhengframework.cache;

import com.google.inject.AbstractModule;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.spi.CachingProvider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public abstract class InfinispanModule extends AbstractModule {

  @Override
  protected void configure() {
    CachingProvider cachingProvider =
        Caching.getCachingProvider();
    CacheManager cacheManager = cacheManager(cachingProvider);

    configureCacheManager(cacheManager);
    bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
    bind(CacheManager.class).toInstance(cacheManager);
  }

  protected CacheManager cacheManager(CachingProvider cachingProvider) {
    return cachingProvider.getCacheManager();
  }

  protected abstract void configureCacheManager(CacheManager cacheManager);
}
