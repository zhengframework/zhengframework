package com.github.zhengframework.cache;

import com.github.zhengframework.core.Configurer;
import com.google.inject.AbstractModule;
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

public class CacheModule extends AbstractModule {

  @Override
  protected void configure() {
    OptionalBinder.newOptionalBinder(binder(), CachingProvider.class)
        .setDefault().toProvider(CachingProviderProvider.class);

    OptionalBinder.newOptionalBinder(binder(),new TypeLiteral<Configurer<CacheManager>>(){});
    OptionalBinder.newOptionalBinder(binder(), CacheManager.class)
        .setDefault()
        .toProvider(CacheManagerProvider.class);

    OptionalBinder.newOptionalBinder(binder(), CacheResolverFactory.class)
        .setDefault().toProvider(CacheResolverFactoryProvider.class);

    OptionalBinder.newOptionalBinder(binder(), CacheKeyGenerator.class)
        .setDefault().to(DefaultCacheKeyGenerator.class);

    this.bind(new TypeLiteral<CacheContextSource<MethodInvocation>>() {
    }).to(CacheLookupUtil.class);
    CachePutInterceptor cachePutInterceptor = new CachePutInterceptor();
    this.requestInjection(cachePutInterceptor);
    this.bindInterceptor(
        Matchers.annotatedWith(CachePut.class), Matchers.any(), cachePutInterceptor);
    this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(CachePut.class),
        cachePutInterceptor);
    CacheResultInterceptor cacheResultInterceptor = new CacheResultInterceptor();
    this.requestInjection(cacheResultInterceptor);
    this.bindInterceptor(Matchers.annotatedWith(CacheResult.class), Matchers.any(),
        cacheResultInterceptor);
    this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(CacheResult.class),
        cacheResultInterceptor);
    CacheRemoveEntryInterceptor cacheRemoveEntryInterceptor = new CacheRemoveEntryInterceptor();
    this.requestInjection(cacheRemoveEntryInterceptor);
    this.bindInterceptor(Matchers.annotatedWith(CacheRemove.class), Matchers.any(),
        cacheRemoveEntryInterceptor);
    this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(CacheRemove.class),
        cacheRemoveEntryInterceptor);
    CacheRemoveAllInterceptor cacheRemoveAllInterceptor = new CacheRemoveAllInterceptor();
    this.requestInjection(cacheRemoveAllInterceptor);
    this.bindInterceptor(Matchers.annotatedWith(CacheRemoveAll.class), Matchers.any(),
        cacheRemoveAllInterceptor);
    this.bindInterceptor(Matchers.any(), Matchers.annotatedWith(CacheRemoveAll.class),
        cacheRemoveAllInterceptor);

  }
}
