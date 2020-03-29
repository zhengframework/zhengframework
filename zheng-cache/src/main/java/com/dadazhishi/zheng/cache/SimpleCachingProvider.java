package com.dadazhishi.zheng.cache;

import java.net.URI;
import java.util.Properties;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

public class SimpleCachingProvider implements CachingProvider {

  private final SimpleCacheManager cacheManager;

  public SimpleCachingProvider() {
    cacheManager = new SimpleCacheManager(this);
  }

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
    return cacheManager;
  }

  @Override
  public ClassLoader getDefaultClassLoader() {
    return SimpleCachingProvider.class.getClassLoader();
  }

  @Override
  public URI getDefaultURI() {
    return URI.create("map://default");
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
    cacheManager.close();
  }

  @Override
  public void close(ClassLoader classLoader) {
    cacheManager.close();
  }

  @Override
  public void close(URI uri, ClassLoader classLoader) {
    cacheManager.close();
  }

  @Override
  public boolean isSupported(OptionalFeature optionalFeature) {
    return false;
  }
}
