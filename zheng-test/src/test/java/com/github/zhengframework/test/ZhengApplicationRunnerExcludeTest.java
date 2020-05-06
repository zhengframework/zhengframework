package com.github.zhengframework.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
@WithZhengApplication(
    moduleClass = {User.Module.class},
    excludeModuleClass = {User.Module.class})
public class ZhengApplicationRunnerExcludeTest {

  @Inject(optional = true)
  private User user;

  @Inject(optional = true)
  private Server server;

  @Inject private Injector injector;

  @Test
  @WithZhengApplication(moduleClass = {User.Module.class})
  public void testA() {
    assertNotNull(injector);
    assertNull(user);
    assertNull(server);
  }

  @Test
  @WithZhengApplication(
      moduleClass = {
        Server.Module.class,
      })
  public void testB() {
    assertNotNull(injector);
    assertNull(user);
    assertNotNull(server);
  }

  @Test
  @WithZhengApplication(moduleClass = {User.Module.class, Server.Module.class})
  public void testAB() {
    assertNotNull(injector);
    assertNull(user);
    assertNotNull(server);
  }
}
