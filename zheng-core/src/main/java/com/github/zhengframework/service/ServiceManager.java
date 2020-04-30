package com.github.zhengframework.service;

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

  private boolean starting;

  private boolean stopping;

  @Inject
  public ServiceManager(ServiceClassScanner serviceClassScanner) {
    starting = false;
    stopping = false;
    List<Service> copyList = new ArrayList<>();
    serviceClassScanner.accept(copyList::add);
    copyList.sort(Comparator.comparing(Service::order).reversed());
    services = ImmutableList.copyOf(copyList);
  }

  public void start() throws Exception {
    if (!starting) {
      for (Service service : services) {
        try {
          log.info("start service[{}]", service.getClass());
          service.start();
        } catch (Exception e) {
          log.error("start service[{}] fail", service.getClass(), e);
        }
      }
      starting = true;
    }
  }

  public void stop() {
    if (!stopping) {
      stopping = true;
      for (Service service : services) {
        try {
          log.info("stop service[{}]", service.getClass());
          service.stop();
        } catch (Exception e) {
          log.error("fail stop service[{}]", service.getClass(), e);
        }
      }
    }

  }

}
