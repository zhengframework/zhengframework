package com.dadazhishi.zheng.metrics.example;

import com.dadazhishi.zheng.metrics.MetricsModule;
import com.dadazhishi.zheng.service.Run;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class MetricsModuleExample {


  public static void main(String[] args) throws Exception {
    final Injector injector = Guice.createInjector(
        new MetricsModule()
    );

    // start services
    injector.getInstance(Run.class).start();
  }

}
