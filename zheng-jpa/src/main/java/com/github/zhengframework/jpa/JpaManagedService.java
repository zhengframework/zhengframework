package com.github.zhengframework.jpa;

import com.github.zhengframework.core.Service;
import javax.inject.Inject;

public class JpaManagedService implements Service {

  private final JpaService jpaService;

  @Inject
  public JpaManagedService(
      JpaService jpaService) {
    this.jpaService = jpaService;
  }

  @Override
  public int priority() {
    return 0;
  }

  @Override
  public void start() throws Exception {
    jpaService.start();
  }

  @Override
  public void stop() throws Exception {
    jpaService.stop();
  }
}
