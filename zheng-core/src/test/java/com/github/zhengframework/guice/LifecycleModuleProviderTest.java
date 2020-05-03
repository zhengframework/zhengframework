package com.github.zhengframework.guice;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.core.ModuleProvider;
import java.util.Iterator;
import java.util.ServiceLoader;
import org.junit.Test;

public class LifecycleModuleProviderTest {

  @Test
  public void getModule() {
    Iterator<ModuleProvider> iterator = ServiceLoader.load(ModuleProvider.class).iterator();
    assertEquals(LifecycleModule.class,iterator.next().getModule().getClass());
  }
}