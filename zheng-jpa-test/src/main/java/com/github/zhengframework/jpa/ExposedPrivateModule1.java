package com.github.zhengframework.jpa;

import com.github.zhengframework.guice.ExposedPrivateModule;
import com.github.zhengframework.jpa.a.Work;

public class ExposedPrivateModule1 extends ExposedPrivateModule {

  @Override
  protected void configure() {
    bind(Work.class);
    expose(Work.class);
  }
}
