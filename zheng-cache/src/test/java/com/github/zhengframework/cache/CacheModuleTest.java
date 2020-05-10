package com.github.zhengframework.cache;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.annotation.CacheResult;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@Slf4j
@RunWith(ZhengApplicationRunner.class)
@WithZhengApplication(moduleClass = MyModule.class)
public class CacheModuleTest {

  @Inject
  CacheManager cacheManager;
  @Inject
  Service service;

  @Test
  public void factory() {
    for (String cacheName : cacheManager.getCacheNames()) {
      log.info("{}", cacheName);
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

  public static class Service {

    int times;

    @CacheResult(cacheName = "annotations")
    public Integer get() {
      return ++times;
    }
  }
}
