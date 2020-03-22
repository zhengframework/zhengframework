package com.dadazhishi.zheng.cache;

import com.google.inject.AbstractModule;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.spi.CachingProvider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public abstract class JCSCacheModule extends AbstractModule {

  @Override
  protected void configure() {
    CachingProvider cachingProvider =
        Caching.getCachingProvider();
    CacheManager cacheManager = cachingProvider.getCacheManager(
        cachingProvider.getDefaultURI(),
        JCSCacheModule.class.getClassLoader(),
        cachingProvider.getDefaultProperties());
    configureCacheManager(cacheManager);
    bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
    bind(CacheManager.class).toInstance(cacheManager);
  }

  protected abstract void configureCacheManager(CacheManager cacheManager);
}
