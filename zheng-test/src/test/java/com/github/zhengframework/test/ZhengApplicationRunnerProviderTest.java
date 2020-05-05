package com.github.zhengframework.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class ZhengApplicationRunnerProviderTest {

  @Inject(optional = true)
  private Apple apple;

  @Inject
  private Injector injector;

  @Test
  @WithZhengApplication()
  public void testInclude() {
    assertNotNull(injector);
    assertNotNull(apple);
  }

  @Test
  @WithZhengApplication(excludeModuleProviderClass = AppleModuleProvider.class)
  public void testExclude() {
    assertNotNull(injector);
    assertNull(apple);
  }
}
