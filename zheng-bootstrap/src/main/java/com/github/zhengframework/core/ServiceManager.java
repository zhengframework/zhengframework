package com.github.zhengframework.core;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
public class ServiceManager {

  private List<Service> services;

  @Inject
  public ServiceManager(ServiceClassScanner serviceClassScanner) {
    List<Service> copyList = new ArrayList<>();
    serviceClassScanner.accept(copyList::add);
    copyList.sort(Comparator.comparing(Service::priority).reversed());
    services = ImmutableList.copyOf(copyList);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        log.info("stop by ShutdownHook");
        ServiceManager.this.stop();
      }
    });
  }

  public void start() throws Exception {
    for (Service service : services) {
      try {
        log.info("start service[{}]", service.getClass());
        service.start();
      } catch (Exception e) {
        log.error("start service[{}] fail", service.getClass(), e);
      }
    }
  }

  public void stop() {
    for (Service service : services) {
      try {
        log.info("stop service[{}]", service.getClass());
        service.stop();
      } catch (Exception e) {
        log.error("fail start service[{}]", service.getClass(), e);
      }
    }
  }

}
