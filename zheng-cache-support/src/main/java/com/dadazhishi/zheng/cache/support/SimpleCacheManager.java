package com.dadazhishi.zheng.cache.support;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;

public class SimpleCacheManager implements CacheManager {

  private final SimpleCachingProvider simpleCachingProvider;
  private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>(16);
  private final Set<String> cacheNames = new LinkedHashSet<>(16);
  private boolean isClosed = false;

  public SimpleCacheManager(
      SimpleCachingProvider simpleCachingProvider) {
    this.simpleCachingProvider = simpleCachingProvider;
  }

  @Override
  public CachingProvider getCachingProvider() {
    return simpleCachingProvider;
  }

  @Override
  public URI getURI() {
    return URI.create("map://default");
  }

  @Override
  public ClassLoader getClassLoader() {
    return SimpleCacheManager.class.getClassLoader();
  }

  @Override
  public Properties getProperties() {
    return null;
  }

  @Override
  public <K, V, C extends Configuration<K, V>> Cache<K, V> createCache(String name, C c)
      throws IllegalArgumentException {
    return getCache(name);
  }

  @Override
  public <K, V> Cache<K, V> getCache(String name, Class<K> aClass, Class<V> aClass1) {
    return getCache(name);
  }

  @Override
  public <K, V> Cache<K, V> getCache(String name) {
    Cache<K, V> cache = this.caches.get(name);
    if (cache == null) {
      this.caches.computeIfAbsent(name, key -> new SimpleCache<>(SimpleCacheManager.this, name));
      synchronized (this.cacheNames) {
        this.cacheNames.add(name);
      }
    }
    return this.caches.get(name);
  }

  @Override
  public Iterable<String> getCacheNames() {
    synchronized (this.cacheNames) {
      return Collections.unmodifiableSet(this.cacheNames);
    }
  }

  @Override
  public void destroyCache(String s) {
    synchronized (this.cacheNames) {
      cacheNames.remove(s);
    }
  }

  @Override
  public void enableManagement(String s, boolean b) {

  }

  @Override
  public void enableStatistics(String s, boolean b) {

  }

  @Override
  public void close() {
    synchronized (this.cacheNames) {
      isClosed = true;
      cacheNames.clear();
    }
  }

  @Override
  public boolean isClosed() {
    return isClosed;
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }
}
