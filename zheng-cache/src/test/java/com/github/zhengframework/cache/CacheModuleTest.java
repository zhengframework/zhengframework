package com.github.zhengframework.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableSet;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResolverFactory;
import javax.cache.annotation.CacheResult;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
import org.cache2k.Cache2kBuilder;
import org.cache2k.jcache.ExtendedMutableConfiguration;
import org.cache2k.jcache.provider.JCacheProvider;
import org.jsr107.ri.annotations.DefaultCacheResolverFactory;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CacheModuleTest {

  @Inject
  CacheManager cacheManager;
  @Inject
  Service service;

  @Before
  public void beforeMethod() {
    Module module = Modules.override(new CacheAnnotationsModule()).with(new JCacheModule());
    Guice.createInjector(module).injectMembers(this);
//    Guice.createInjector(new CacheAnnotationsModule()).injectMembers(this);
  }

  @Test
  public void factory() {
    for (String cacheName : cacheManager.getCacheNames()) {
      System.out.println(cacheName);
    }
    Cache<Integer, Integer> cache = cacheManager.getCache("guice");
    cache.put(1, 1);
    cache.put(2, 2);
    cache.put(3, 3);
    Map<Integer, Integer> result = cache.getAll(ImmutableSet.of(1, 2, 3));
    Assert.assertEquals(ImmutableSet.of(1, 2, 3), ImmutableSet.copyOf(result.values()));

  }

  @Test
  public void annotations() {
    for (int i = 0; i < 10; i++) {
      assertThat(service.get(), is(1));
    }
    assertThat(service.times, is(1));
  }

  static class Service {

    int times;

    @CacheResult(cacheName = "annotations")
    public Integer get() {
      return ++times;
    }
  }


  static final class JCacheModule extends AbstractModule {

    @Override
    protected void configure() {
      configureCachingProvider();
    }

    void configureCachingProvider() {
      CachingProvider cachingProvider =
          Caching.getCachingProvider(JCacheProvider.class.getName());
      CacheManager cacheManager = cachingProvider.getCacheManager();
      cacheManager.destroyCache("guice");
      cacheManager.createCache("guice", ExtendedMutableConfiguration
          .of(new Cache2kBuilder<Integer, Integer>() {
          }
              .entryCapacity(1000).expireAfterWrite(10, TimeUnit.SECONDS)));
      bind(CacheResolverFactory.class).toInstance(new DefaultCacheResolverFactory(cacheManager));
      bind(CacheManager.class).toInstance(cacheManager);
    }
  }

}
