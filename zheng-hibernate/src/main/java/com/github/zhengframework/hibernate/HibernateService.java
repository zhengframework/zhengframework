package com.github.zhengframework.hibernate;

import com.github.zhengframework.core.Service;
import javax.inject.Inject;

public class HibernateService implements Service {

  private final HibernatePersistService hibernatePersistService;

  @Inject
  private HibernateService(HibernatePersistService hibernatePersistService) {
    this.hibernatePersistService = hibernatePersistService;
  }

  @Override
  public int priority() {
    return 0;
  }

  @Override
  public void start() throws Exception {
    hibernatePersistService.start();
  }

  @Override
  public void stop() throws Exception {
    hibernatePersistService.stop();
  }
}
