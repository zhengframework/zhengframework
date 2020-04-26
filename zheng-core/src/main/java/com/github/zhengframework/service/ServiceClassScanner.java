package com.github.zhengframework.service;

import com.github.zhengframework.guice.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;

public class ServiceClassScanner extends ClassScanner<Service> {

  @Inject
  public ServiceClassScanner(Injector injector) {
    super(injector, Service.class);
  }
}
