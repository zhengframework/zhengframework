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

import java.net.URI;
import java.util.Properties;
import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import org.kohsuke.MetaInfServices;

@MetaInfServices
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
