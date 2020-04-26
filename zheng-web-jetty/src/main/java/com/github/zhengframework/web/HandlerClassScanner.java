package com.github.zhengframework.web;

import com.github.zhengframework.guice.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import org.eclipse.jetty.server.Handler;

public class HandlerClassScanner extends ClassScanner<Handler> {

  @Inject
  public HandlerClassScanner(Injector injector) {
    super(injector, Handler.class);
  }
}
