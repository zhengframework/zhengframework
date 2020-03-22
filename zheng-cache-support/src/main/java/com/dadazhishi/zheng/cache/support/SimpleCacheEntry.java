package com.dadazhishi.zheng.cache.support;

import javax.cache.Cache;

public class SimpleCacheEntry<K, V> implements Cache.Entry<K, V> {

  private final K k;
  private final V v;

  public SimpleCacheEntry(K k, V v) {
    this.k = k;
    this.v = v;
  }

  @Override
  public K getKey() {
    return k;
  }

  @Override
  public V getValue() {
    return v;
  }

  @Override
  public <T> T unwrap(Class<T> clazz) {
    return null;
  }
}
