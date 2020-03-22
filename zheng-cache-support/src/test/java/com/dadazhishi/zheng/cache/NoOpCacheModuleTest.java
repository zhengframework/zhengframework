package com.dadazhishi.zheng.cache;

import com.dadazhishi.zheng.cache.support.NoOpCacheModule;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import java.util.HashSet;
import java.util.Set;
import javax.cache.CacheManager;
import javax.inject.Inject;
import org.jsr107.ri.annotations.guice.module.CacheAnnotationsModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class NoOpCacheModuleTest {

  @Inject
  CacheManager cacheManager;

  @Before
  public void beforeMethod() {
    Module module = Modules.override(new CacheAnnotationsModule()).with(new NoOpCacheModule());

    Guice.createInjector(module).injectMembers(this);
//
  }

  @Test
  public void factory() {
    Set<String> cacheNames = new HashSet<>();
    for (int i = 0; i < 30; i++) {
      cacheNames.add("cache-" + i);
      cacheManager.getCache("cache-" + i);
    }
    Assert.assertEquals(ImmutableSet.copyOf(cacheNames),
        ImmutableSet.copyOf(cacheManager.getCacheNames()));
  }


}
