package com.github.zhengframework.guice;

import static org.junit.Assert.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Before;
import org.junit.Test;

public class DestroyableTest {

  Injector injector;
  DestroyableManager manager;

  @Before
  public void setUp() throws Exception {
    injector = Guice.createInjector(new LifecycleModule());
    manager = injector.getInstance(DestroyableManager.class);
  }

  @Test
  public void testCall() throws Exception {
    Bean bean = injector.getInstance(Bean.class);
    manager.destroy();
    assertEquals(1, bean.counter);

  }

  public static class Bean implements Destroyable {
    int counter;

    @Override
    public void preDestroy() throws Exception {
      counter++;
    }
  }
}
