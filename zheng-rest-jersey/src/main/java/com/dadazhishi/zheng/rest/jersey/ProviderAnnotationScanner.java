package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.service.AnnotationScanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;

public class ProviderAnnotationScanner extends AnnotationScanner<Object> {

  @Inject
  public ProviderAnnotationScanner(Injector injector) {
    super(injector, Provider.class);
  }
}
