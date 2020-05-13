package com.github.zhengframework.rest;

import com.github.zhengframework.bootstrap.ZhengApplication;
import com.github.zhengframework.bootstrap.ZhengApplicationBuilder;

public class FirstUseZhengFramework {

  public static void main(String[] args) throws Exception {
    ZhengApplication application = ZhengApplicationBuilder.create()
        .addModule(new FirstModule())
        .build();
    application.start();
  }
}
