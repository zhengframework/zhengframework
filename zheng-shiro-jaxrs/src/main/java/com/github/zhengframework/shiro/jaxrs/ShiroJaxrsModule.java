package com.github.zhengframework.shiro.jaxrs;

import com.google.inject.AbstractModule;
import org.apache.shiro.web.jaxrs.ShiroFeature;

public class ShiroJaxrsModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ShiroFeature.class);
  }
}
