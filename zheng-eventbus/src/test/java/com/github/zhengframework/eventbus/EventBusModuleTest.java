package com.github.zhengframework.eventbus;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.test.WithZhengApplication;
import com.github.zhengframework.test.ZhengApplicationRunner;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class EventBusModuleTest {

  @Inject
  Injector injector;

  @WithZhengApplication(moduleClass = MyModule.class)
  @Test
  public void configure() throws Exception {
    Pub pub = injector.getInstance(Pub.class);
    for (int i = 0; i < 10; i++) {
      pub.publish();
    }
    Sub sub = injector.getInstance(Sub.class);
    assertEquals(10, sub.getCount());
  }

}