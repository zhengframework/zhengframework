package com.dadazhishi.zheng.rest.jersey;

import com.dadazhishi.zheng.service.Scanner;
import com.google.inject.Injector;
import javax.ws.rs.core.Feature;

public class FeatureScanner extends Scanner<Feature> {

  public FeatureScanner(Injector injector) {
    super(injector, Feature.class);
  }
}