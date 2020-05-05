package com.github.zhengframework.cache.simple;

/*-
 * #%L
 * zheng-cache
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;

public class SimpleCache<K, V> implements Cache<K, V> {

  private final ConcurrentMap<K, V> caches = new ConcurrentHashMap<>(16);
  private final SimpleCacheManager simpleCacheManager;
  private final String name;

  private boolean isClosed = false;

  public SimpleCache(SimpleCacheManager simpleCacheManager, String name) {
    this.simpleCacheManager = simpleCacheManager;
    this.name = name;
  }

  @Override
  public V get(K k) {
    return caches.get(k);
  }

  @Override
  public Map<K, V> getAll(Set<? extends K> set) {
    HashMap<K, V> map = new HashMap<>();
    for (K k : set) {
      map.put(k, get(k));
    }
    return map;
  }

  @Override
  public boolean containsKey(K k) {
    return caches.containsKey(k);
  }

  @Override
  public void loadAll(Set<? extends K> set, boolean b, CompletionListener completionListener) {
  }

  @Override
  public void put(K k, V v) {
    caches.put(k, v);
  }

  @Override
  public V getAndPut(K k, V v) {
    V v1 = caches.get(k);
    caches.replace(k, v);
    return v1;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {
    for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public boolean putIfAbsent(K k, V v) {
    caches.putIfAbsent(k, v);
    return true;
  }

  @Override
  public boolean remove(K k) {
    caches.remove(k);
    return true;
  }

  @Override
  public boolean remove(K k, V v) {
    caches.remove(k, v);
    return true;
  }

  @Override
  public V getAndRemove(K k) {
    V v = caches.get(k);
    caches.remove(k);
    return v;
  }

  @Override
  public boolean replace(K k, V v, V v1) {
    caches.replace(k, v1);
    return true;
  }

  @Override
  public boolean replace(K k, V v) {
    caches.replace(k, v);
    return true;
  }

  @Override
  public V getAndReplace(K k, V v) {
    V v1 = caches.get(k);
    caches.replace(k, v);
    return v1;
  }

  @Override
  public void removeAll(Set<? extends K> set) {
    for (K k : set) {
      caches.remove(k);
    }
  }

  @Override
  public void removeAll() {
    caches.clear();
  }

  @Override
  public void clear() {
    caches.clear();
  }

  @Override
  public <C extends Configuration<K, V>> C getConfiguration(Class<C> aClass) {
    return null;
  }

  @Override
  public <T> T invoke(K k, EntryProcessor<K, V, T> entryProcessor, Object... objects)
      throws EntryProcessorException {
    return null;
  }

  @Override
  public <T> Map<K, EntryProcessorResult<T>> invokeAll(
      Set<? extends K> set, EntryProcessor<K, V, T> entryProcessor, Object... objects) {
    return null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public CacheManager getCacheManager() {
    return simpleCacheManager;
  }

  @Override
  public void close() {
    isClosed = true;
    caches.clear();
  }

  @Override
  public boolean isClosed() {
    return isClosed;
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }

  @Override
  public void registerCacheEntryListener(
      CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
  }

  @Override
  public void deregisterCacheEntryListener(
      CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {
  }

  @Override
  public Iterator<Entry<K, V>> iterator() {
    List<Entry<K, V>> list =
        caches.entrySet().stream()
            .map(entry -> new SimpleCacheEntry<>(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    return list.iterator();
  }
}
