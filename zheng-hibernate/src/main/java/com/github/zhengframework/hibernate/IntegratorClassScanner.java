package com.github.zhengframework.hibernate;

import com.github.zhengframework.service.ClassScanner;
import com.google.inject.Injector;
import org.hibernate.integrator.spi.Integrator;

public class IntegratorClassScanner extends ClassScanner<Integrator> {

  public IntegratorClassScanner(Injector injector) {
    super(injector, Integrator.class);
  }
}
