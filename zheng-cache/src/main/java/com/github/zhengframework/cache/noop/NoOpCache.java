package com.github.zhengframework.cache.noop;

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

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.configuration.CacheEntryListenerConfiguration;
import javax.cache.configuration.Configuration;
import javax.cache.integration.CompletionListener;
import javax.cache.processor.EntryProcessor;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;

public class NoOpCache<K, V> implements Cache<K, V> {

  private final CacheManager cacheManager;
  private String name;

  public NoOpCache(CacheManager cacheManager, String name) {
    this.cacheManager = cacheManager;
    this.name = name;
  }

  @Override
  public V get(K k) {
    return null;
  }

  @Override
  public Map<K, V> getAll(Set<? extends K> set) {
    return null;
  }

  @Override
  public boolean containsKey(K k) {
    return false;
  }

  @Override
  public void loadAll(Set<? extends K> set, boolean b, CompletionListener completionListener) {}

  @Override
  public void put(K k, V v) {}

  @Override
  public V getAndPut(K k, V v) {
    return null;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> map) {}

  @Override
  public boolean putIfAbsent(K k, V v) {
    return false;
  }

  @Override
  public boolean remove(K k) {
    return false;
  }

  @Override
  public boolean remove(K k, V v) {
    return false;
  }

  @Override
  public V getAndRemove(K k) {
    return null;
  }

  @Override
  public boolean replace(K k, V v, V v1) {
    return false;
  }

  @Override
  public boolean replace(K k, V v) {
    return false;
  }

  @Override
  public V getAndReplace(K k, V v) {
    return null;
  }

  @Override
  public void removeAll(Set<? extends K> set) {}

  @Override
  public void removeAll() {}

  @Override
  public void clear() {}

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
    return cacheManager;
  }

  @Override
  public void close() {}

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> aClass) {
    return null;
  }

  @Override
  public void registerCacheEntryListener(
      CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {}

  @Override
  public void deregisterCacheEntryListener(
      CacheEntryListenerConfiguration<K, V> cacheEntryListenerConfiguration) {}

  public Iterator<Entry<K, V>> iterator() {
    return Collections.emptyIterator();
  }
}
