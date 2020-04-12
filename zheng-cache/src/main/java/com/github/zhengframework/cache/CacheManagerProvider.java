package com.github.zhengframework.cache;

import com.github.zhengframework.core.Configurer;
import java.util.Optional;
import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
import javax.inject.Provider;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CacheManagerProvider implements Provider<CacheManager> {

  private final CachingProvider cachingProvider;
  private final Optional<Configurer<CacheManager>> cacheManagerConfigurer;

  @Inject
  public CacheManagerProvider(CachingProvider cachingProvider,
      Optional<Configurer<CacheManager>> cacheManagerConfigurer) {
    this.cachingProvider = cachingProvider;
    this.cacheManagerConfigurer = cacheManagerConfigurer;
  }

  @Override
  public CacheManager get() {
    CacheManager cacheManager = cachingProvider
        .getCacheManager(cachingProvider.getDefaultURI(), cachingProvider.getDefaultClassLoader());
    cacheManagerConfigurer
        .ifPresent(managerConfigurer -> managerConfigurer.configure(cacheManager));
    return cacheManager;
  }
}
