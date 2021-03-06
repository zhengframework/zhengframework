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

import javax.cache.CacheManager;
import javax.cache.annotation.CacheResolverFactory;
import javax.inject.Inject;
import javax.inject.Provider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;

public class CacheResolverFactoryProvider implements Provider<CacheResolverFactory> {

  private final CacheManager cacheManager;

  @Inject
  public CacheResolverFactoryProvider(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @Override
  public CacheResolverFactory get() {
    return new DefaultCacheResolverFactory(cacheManager);
  }
}
