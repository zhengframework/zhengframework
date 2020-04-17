package com.github.zhengframework.hibernate;

import com.github.zhengframework.core.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import org.hibernate.integrator.spi.Integrator;

public class IntegratorClassScanner extends ClassScanner<Integrator> {

  @Inject
  public IntegratorClassScanner(Injector injector) {
    super(injector, Integrator.class);
  }
}
