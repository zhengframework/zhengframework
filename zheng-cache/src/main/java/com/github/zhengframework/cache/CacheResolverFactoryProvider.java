package com.github.zhengframework.cache;

import javax.cache.CacheManager;
import javax.cache.annotation.CacheResolverFactory;
import javax.inject.Inject;
import javax.inject.Provider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public class CacheResolverFactoryProvider implements Provider<CacheResolverFactory> {

  private final CacheManager cacheManager;

  @Inject
  public CacheResolverFactoryProvider(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Override
  public CacheResolverFactory get() {
    return new DefaultCacheResolverFactory(cacheManager);
  }
}
