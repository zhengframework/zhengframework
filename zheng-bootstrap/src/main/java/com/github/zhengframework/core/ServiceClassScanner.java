package com.github.zhengframework.core;

import com.google.inject.Injector;
import javax.inject.Inject;

public class ServiceClassScanner extends ClassScanner<Service> {

  @Inject
  public ServiceClassScanner(Injector injector) {
    super(injector, Service.class);
  }
}
