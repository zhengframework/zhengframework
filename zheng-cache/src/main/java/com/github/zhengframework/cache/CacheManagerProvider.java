package com.github.zhengframework.cache;

import com.github.zhengframework.core.Configurer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import javax.cache.CacheManager;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CacheManagerProvider implements Provider<CacheManager> {

  private final CacheConfig cacheConfig;
  private final CachingProvider cachingProvider;
  private final Optional<Configurer<CacheManager>> cacheManagerConfigurer;

  @Inject
  public CacheManagerProvider(CacheConfig cacheConfig,
      CachingProvider cachingProvider,
      Optional<Configurer<CacheManager>> cacheManagerConfigurer) {
    this.cacheConfig = cacheConfig;
    this.cachingProvider = cachingProvider;
    this.cacheManagerConfigurer = cacheManagerConfigurer;
  }

  @Override
  public CacheManager get() {
    CacheManager cacheManager;

    String cacheManagerResource = StringUtils.trimToEmpty(cacheConfig.getCacheManagerResource());
    if (!cacheManagerResource.isEmpty()) {
      URL resource = Thread.currentThread().getContextClassLoader()
          .getResource(cacheManagerResource);
      try {
        cacheManager = cachingProvider
            .getCacheManager(Objects.requireNonNull(resource).toURI(), cachingProvider.getDefaultClassLoader());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    } else {
      cacheManager = cachingProvider
          .getCacheManager(cachingProvider.getDefaultURI(),
              cachingProvider.getDefaultClassLoader());
    }
    cacheManagerConfigurer
        .ifPresent(managerConfigurer -> managerConfigurer.configure(cacheManager));
    return cacheManager;
  }
}
