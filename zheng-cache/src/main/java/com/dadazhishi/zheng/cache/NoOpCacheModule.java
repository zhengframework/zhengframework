package com.dadazhishi.zheng.cache;

import com.google.inject.AbstractModule;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.spi.CachingProvider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public class NoOpCacheModule extends AbstractModule {

  @Override
  protected void configure() {
    CachingProvider cachingProvider =
        Caching.getCachingProvider(NoOpCachingProvider.class.getName());
    CacheManager cacheManager = cachingProvider.getCacheManager();
    bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
    bind(CacheManager.class).toInstance(cacheManager);
  }

}
