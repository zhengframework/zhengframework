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

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.core.Configurer;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.OptionalBinder;
import javax.cache.CacheManager;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.CacheResult;
import javax.cache.spi.CachingProvider;
import org.aopalliance.intercept.MethodInvocation;
import org.jsr107.ri.annotations.CacheContextSource;
import org.jsr107.ri.annotations.DefaultCacheKeyGenerator;
import org.jsr107.ri.annotations.guice.CacheLookupUtil;
import org.jsr107.ri.annotations.guice.CachePutInterceptor;
import org.jsr107.ri.annotations.guice.CacheRemoveAllInterceptor;
import org.jsr107.ri.annotations.guice.CacheRemoveEntryInterceptor;
import org.jsr107.ri.annotations.guice.CacheResultInterceptor;

public class CacheModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Configuration configuration = getConfiguration();
    CacheConfig cacheConfig =
        ConfigurationBeanMapper.resolve(configuration, CacheConfig.ZHENG_CACHE, CacheConfig.class);
    bind(CacheConfig.class).toInstance(cacheConfig);

    OptionalBinder.newOptionalBinder(binder(), CachingProvider.class)
        .setDefault()
        .toProvider(CachingProviderProvider.class);

    OptionalBinder.newOptionalBinder(binder(), new TypeLiteral<Configurer<CacheManager>>() {});
    OptionalBinder.newOptionalBinder(binder(), CacheManager.class)
        .setDefault()
        .toProvider(CacheManagerProvider.class);

    OptionalBinder.newOptionalBinder(binder(), CacheResolverFactory.class)
        .setDefault()
        .toProvider(CacheResolverFactoryProvider.class);

    OptionalBinder.newOptionalBinder(binder(), CacheKeyGenerator.class)
        .setDefault()
        .to(DefaultCacheKeyGenerator.class);

    this.bind(new TypeLiteral<CacheContextSource<MethodInvocation>>() {}).to(CacheLookupUtil.class);
    CachePutInterceptor cachePutInterceptor = new CachePutInterceptor();
    this.requestInjection(cachePutInterceptor);
    this.bindInterceptor(
        Matchers.annotatedWith(CachePut.class), Matchers.any(), cachePutInterceptor);
    this.bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(CachePut.class), cachePutInterceptor);
    CacheResultInterceptor cacheResultInterceptor = new CacheResultInterceptor();
    this.requestInjection(cacheResultInterceptor);
    this.bindInterceptor(
        Matchers.annotatedWith(CacheResult.class), Matchers.any(), cacheResultInterceptor);
    this.bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(CacheResult.class), cacheResultInterceptor);
    CacheRemoveEntryInterceptor cacheRemoveEntryInterceptor = new CacheRemoveEntryInterceptor();
    this.requestInjection(cacheRemoveEntryInterceptor);
    this.bindInterceptor(
        Matchers.annotatedWith(CacheRemove.class), Matchers.any(), cacheRemoveEntryInterceptor);
    this.bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(CacheRemove.class), cacheRemoveEntryInterceptor);
    CacheRemoveAllInterceptor cacheRemoveAllInterceptor = new CacheRemoveAllInterceptor();
    this.requestInjection(cacheRemoveAllInterceptor);
    this.bindInterceptor(
        Matchers.annotatedWith(CacheRemoveAll.class), Matchers.any(), cacheRemoveAllInterceptor);
    this.bindInterceptor(
        Matchers.any(), Matchers.annotatedWith(CacheRemoveAll.class), cacheRemoveAllInterceptor);
  }
}
