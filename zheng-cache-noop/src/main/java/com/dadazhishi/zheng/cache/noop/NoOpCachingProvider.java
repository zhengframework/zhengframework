package com.dadazhishi.zheng.cache.noop;

import java.net.URI;
import java.util.Properties;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;

public class NoOpCachingProvider implements CachingProvider {

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader, Properties properties) {
    return new NoOpCacheManager(this);
  }

  @Override
  public ClassLoader getDefaultClassLoader() {
    return NoOpCachingProvider.class.getClassLoader();
  }

  @Override
  public URI getDefaultURI() {
    return URI.create("noop://1");
  }

  @Override
  public Properties getDefaultProperties() {
    return new Properties();
  }

  @Override
  public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
    return new NoOpCacheManager(this);
  }

  @Override
  public CacheManager getCacheManager() {
    return new NoOpCacheManager(this);
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
