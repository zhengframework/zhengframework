package com.github.zhengframework.cache;

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
  public CacheManagerProvider(
      CacheConfig cacheConfig,
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
      URL resource =
          Thread.currentThread().getContextClassLoader().getResource(cacheManagerResource);
      try {
        cacheManager =
            cachingProvider.getCacheManager(
                Objects.requireNonNull(resource).toURI(), cachingProvider.getDefaultClassLoader());
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    } else {
      cacheManager =
          cachingProvider.getCacheManager(
              cachingProvider.getDefaultURI(), cachingProvider.getDefaultClassLoader());
    }
    cacheManagerConfigurer.ifPresent(
        managerConfigurer -> managerConfigurer.configure(cacheManager));
    return cacheManager;
  }
}
