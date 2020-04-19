package com.github.zhengframework.jpa;

import com.github.zhengframework.guice.ExposedPrivateModule;
import com.github.zhengframework.jpa.b.Work2;

public class ExposedPrivateModule2 extends ExposedPrivateModule {

  @Override
  protected void configure() {
    bind(Work2.class);
    expose(Work2.class);
  }
}
