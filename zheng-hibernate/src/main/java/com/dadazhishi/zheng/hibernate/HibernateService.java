package com.dadazhishi.zheng.hibernate;

import com.dadazhishi.zheng.service.ServiceRegistry;
import com.google.common.util.concurrent.AbstractIdleService;
import javax.inject.Inject;

@SuppressWarnings("UnstableApiUsage")
public class HibernateService extends AbstractIdleService {

  private final HibernatePersistService hibernatePersistService;

  @Inject
  private HibernateService(ServiceRegistry serviceRegistry,
      HibernatePersistService hibernatePersistService) {
    this.hibernatePersistService = hibernatePersistService;
    serviceRegistry.add(this);
  }

  @Override
  protected void startUp() throws Exception {
    hibernatePersistService.start();
  }

  @Override
  protected void shutDown() throws Exception {
    hibernatePersistService.stop();
  }
}
