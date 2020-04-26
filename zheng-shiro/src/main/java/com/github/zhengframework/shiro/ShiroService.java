package com.github.zhengframework.shiro;

import com.github.zhengframework.guice.ClassScanner;
import com.github.zhengframework.service.Service;
import com.google.inject.Injector;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.Destroyable;

@Slf4j
public class ShiroService implements Service {

  private final Injector injector;

  @Inject
  public ShiroService(Injector injector) {
    this.injector = injector;
  }

  @Override
  public int order() {
    return 0;
  }

  @Override
  public void start() throws Exception {

  }

  @Override
  public void stop() throws Exception {
    ClassScanner<Destroyable> classScanner = new ClassScanner<>(injector,
        Destroyable.class);
    classScanner.accept(destroyable -> {
      try {
        destroyable.destroy();
      } catch (Exception e) {
        log.warn("Error destroying component class: " + destroyable.getClass(), e);
      }
    });
  }
}
