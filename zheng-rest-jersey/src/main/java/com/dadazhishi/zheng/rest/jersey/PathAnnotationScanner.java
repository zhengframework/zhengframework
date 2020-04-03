package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.service.AnnotationScanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import javax.ws.rs.Path;

public class PathAnnotationScanner extends AnnotationScanner<Object> {

  @Inject
  public PathAnnotationScanner(Injector injector) {
    super(injector, Path.class);
  }
}
