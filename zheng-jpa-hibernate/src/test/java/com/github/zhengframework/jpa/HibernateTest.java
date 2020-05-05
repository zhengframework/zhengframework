package com.github.zhengframework.jpa;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class HibernateTest {

  @Inject
  private Injector injector;

  @WithZhengApplication
  @Test
  public void test() throws Exception {
    JpaModuleTest.test(injector);
  }

  @WithZhengApplication(
      configFile = "application_group.properties",
      moduleClass = JpaMultiModule.class,
      excludeModuleProviderClass = JpaModuleProvider.class)
  @Test
  public void testGroup() throws Exception {
    JpaMultiModuleTest.test(injector);
  }
}
