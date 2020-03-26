package com.dadazhishi.zheng.configuration.resolver;

import java.util.Optional;
import java.util.Set;

public class CacheableConfigurationResolver implements ConfigurationResolver {

  private final ConfigurationResolver directResolver;
  private ResolverCache resolverCache;

  public CacheableConfigurationResolver(
      ConfigurationResolver directResolver, ResolverCache resolverCache) {
    this.directResolver = directResolver;
    this.resolverCache = resolverCache;
  }

  public Optional<String> getCache(String key) {
    return resolverCache.getCache(key);
  }

  public void putCache(String key, String value) {
    resolverCache.putCache(key, value);
  }

  @Override
  public Optional<String> get(String key) {
    Optional<String> cache = getCache(key);
    if (cache.isPresent()) {
      return cache;
    }
    Optional<String> stringOptional = directResolver.get(key);
    if (stringOptional.isPresent()) {
      String value = stringOptional.get();
      putCache(key, value);
      return stringOptional;
    }
    return Optional.empty();
  }

  @Override
  public Set<String> keySet() {
    return directResolver.keySet();
  }

  interface ResolverCache {

    Optional<String> getCache(String key);

    void putCache(String key, String value);
  }
}
