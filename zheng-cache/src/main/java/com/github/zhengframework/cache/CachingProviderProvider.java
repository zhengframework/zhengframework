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

import java.util.Arrays;
import java.util.Optional;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
import javax.inject.Provider;
import org.apache.commons.lang3.StringUtils;

public class CachingProviderProvider implements Provider<CachingProvider> {

  private final CacheConfig cacheConfig;

  @Inject
  public CachingProviderProvider(CacheConfig cacheConfig) {
    this.cacheConfig = cacheConfig;
  }

  @Override
  public CachingProvider get() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (StringUtils.isNotEmpty(cacheConfig.getCachingProviderName())) {
      return Caching.getCachingProvider(cacheConfig.getCachingProviderName(),
          classLoader);
    }
    if (StringUtils.isNotEmpty(cacheConfig.getType())) {
      Optional<CachingProviderType> first = Arrays.stream(CachingProviderType.values())
          .filter(type -> type.getType().equalsIgnoreCase(cacheConfig.getType()))
          .findFirst();
      if (first.isPresent()) {
        return Caching.getCachingProvider(first.get().getClassName(),
            classLoader);
      }
    }
    return Caching.getCachingProvider(classLoader);
  }
}
