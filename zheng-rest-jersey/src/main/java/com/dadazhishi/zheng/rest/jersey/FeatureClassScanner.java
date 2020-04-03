package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.service.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import javax.ws.rs.core.Feature;

public class FeatureClassScanner extends ClassScanner<Feature> {

  @Inject
  public FeatureClassScanner(Injector injector) {
    super(injector, Feature.class);
  }
}