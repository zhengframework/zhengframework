package com.github.zhengframework.guice;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import javax.annotation.PreDestroy;
import org.junit.Before;
import org.junit.Test;

public class PreDestroyTest {

  Injector injector;
  DestroyableManager manager;

  @Before
  public void setUp() throws Exception {
    injector = Guice.createInjector(new LifecycleModule());
    manager = injector.getInstance(DestroyableManager.class);
  }

  @Test
  public void testSuccess() throws Exception {
    OkBean bean = injector.getInstance(OkBean.class);
    manager.destroy();
    assertEquals(2, bean.counter);
  }

  @Test
  public void testDoubleDestroy() throws Exception {
    OkBean bean = injector.getInstance(OkBean.class);
    manager.destroy();
    manager.destroy();
    assertEquals(2, bean.counter);
  }

  @Test(expected = ProvisionException.class)
  public void testFail() throws Exception {
    injector.getInstance(KoBean.class);
  }

  @Test
  public void testExceptionFail() throws Exception {
    injector.getInstance(KoExceptionBean.class);
    manager.destroy();
  }

  public static class OkBean {
    private int counter;

    @PreDestroy
    public void init() {
      counter++;
    }

    @PreDestroy
    private void init2() {
      counter++;
    }
  }

  public static class KoBean {
    @PreDestroy
    public void init(Object smth) {
    }
  }

  public static class KoExceptionBean {
    @PreDestroy
    public void init() {
      throw new IllegalStateException("foo");
    }
  }
}
