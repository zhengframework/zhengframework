package com.dadazhishi.zheng.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import java.net.URISyntaxException;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.annotation.CacheResult;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import javax.inject.Inject;
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
    Module module = Modules.override(new CacheAnnotationsModule()).with(new InfinispanModule() {


      @Override
      protected CacheManager cacheManager(CachingProvider cachingProvider) {
        ClassLoader classLoader = InfinispanModule.class.getClassLoader();
        try {
          return cachingProvider
              .getCacheManager(classLoader.getResource("infinispan-jcache.xml").toURI(),
                  classLoader);
        } catch (URISyntaxException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      protected void configureCacheManager(CacheManager cacheManager) {
        MutableConfiguration<Integer, Integer> configuration = new MutableConfiguration<>();
        configuration.setTypes(Integer.class, Integer.class);
        cacheManager.destroyCache("guice");
        cacheManager.createCache("guice", configuration);
      }
    });

    Guice.createInjector(module).injectMembers(this);

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

}
