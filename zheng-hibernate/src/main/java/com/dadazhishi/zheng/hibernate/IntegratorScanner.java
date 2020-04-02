package com.dadazhishi.zheng.hibernate;

import com.dadazhishi.zheng.service.Scanner;
import com.google.inject.Injector;
import org.hibernate.integrator.spi.Integrator;

public class IntegratorScanner extends Scanner<Integrator> {

  public IntegratorScanner(Injector injector) {
    super(injector, Integrator.class);
  }
}
