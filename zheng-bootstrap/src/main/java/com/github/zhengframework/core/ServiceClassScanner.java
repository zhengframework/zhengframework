package com.github.zhengframework.core;

import com.google.inject.Injector;

public class ServiceClassScanner extends ClassScanner<Service> {

  public ServiceClassScanner(Injector injector) {
    super(injector, Service.class);
  }
}
