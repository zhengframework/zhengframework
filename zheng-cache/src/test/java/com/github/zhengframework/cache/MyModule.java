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
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.OptionalBinder;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.CacheManager;
import org.cache2k.Cache2kBuilder;
import org.cache2k.jcache.ExtendedMutableConfiguration;

public class MyModule extends AbstractModule {
  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<Configurer<CacheManager>>() {})
        .setBinding()
        .toInstance(
            cacheManager -> {
              String cacheName = "guice";
              Cache<?, ?> cache = cacheManager.getCache(cacheName);
              if (cache == null) {
                cacheManager.createCache(
                    "guice",
                    ExtendedMutableConfiguration.of(
                        new Cache2kBuilder<Integer, Integer>() {}.entryCapacity(1000)
                            .expireAfterWrite(10, TimeUnit.SECONDS)));
              }
            });
  }
}
