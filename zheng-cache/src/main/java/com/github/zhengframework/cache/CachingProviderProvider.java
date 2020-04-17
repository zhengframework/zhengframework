package com.github.zhengframework.cache;

import java.util.Arrays;
import java.util.Optional;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.commons.lang3.StringUtils;

public class CachingProviderProvider implements Provider<CachingProvider> {

  private final CacheConfig cacheConfig;

  @Inject
  public CachingProviderProvider(CacheConfig cacheConfig) {
    this.cacheConfig = cacheConfig;
  }

  @Override
  public CachingProvider get() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (StringUtils.isNotEmpty(cacheConfig.getCachingProviderName())) {
      return Caching.getCachingProvider(cacheConfig.getCachingProviderName(),
          classLoader);
    }
    if (StringUtils.isNotEmpty(cacheConfig.getType())) {
      Optional<CachingProviderType> first = Arrays.stream(CachingProviderType.values())
          .filter(type -> type.getType().equalsIgnoreCase(cacheConfig.getType()))
          .findFirst();
      if (first.isPresent()) {
        return Caching.getCachingProvider(first.get().getClassName(),
            classLoader);
      }
    }
    return Caching.getCachingProvider(classLoader);
  }
}
