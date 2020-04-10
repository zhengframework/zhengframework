package com.github.zhengframework.cache;

import com.google.inject.AbstractModule;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.spi.CachingProvider;
import org.cache2k.jcache.provider.JCacheProvider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public abstract class Cache2kModule extends AbstractModule {

  @Override
  protected void configure() {
    CachingProvider cachingProvider =
        Caching.getCachingProvider(JCacheProvider.class.getName());
    CacheManager cacheManager = cachingProvider
        .getCacheManager(cachingProvider.getDefaultURI(), cachingProvider.getDefaultClassLoader());
    configureCacheManager(cacheManager);
    bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
    bind(CacheManager.class).toInstance(cacheManager);
  }

  protected abstract void configureCacheManager(CacheManager cacheManager);
}
