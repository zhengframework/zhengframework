package com.dadazhishi.zheng.cache;

import java.net.URI;
import java.util.Properties;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

public class NoOpCachingProvider implements CachingProvider {

  private final NoOpCacheManager cacheManager;

  public NoOpCachingProvider() {
    cacheManager = new NoOpCacheManager(this);
  }

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
    return cacheManager;
  }

  @Override
  public ClassLoader getDefaultClassLoader() {
    return NoOpCachingProvider.class.getClassLoader();
  }

  @Override
  public URI getDefaultURI() {
    return URI.create("noop://default");
  }

  @Override
  public Properties getDefaultProperties() {
    return null;
  }

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
    return cacheManager;
  }

  @Override
  public CacheManager getCacheManager() {
    return cacheManager;
  }

  @Override
  public void close() {

  }

  @Override
  public void close(ClassLoader classLoader) {

  }

  @Override
  public void close(URI uri, ClassLoader classLoader) {

  }

  @Override
  public boolean isSupported(OptionalFeature optionalFeature) {
    return false;
  }
}
