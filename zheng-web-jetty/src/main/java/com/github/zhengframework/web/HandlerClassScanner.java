package com.github.zhengframework.web;

import com.github.zhengframework.service.ClassScanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import org.eclipse.jetty.server.Handler;

/**
 * Walks through the guice injector bindings, visiting each one that is a Handler.
 */
public class HandlerClassScanner extends ClassScanner<Handler> {

  @Inject
  public HandlerClassScanner(Injector injector) {
    super(injector, Handler.class);
  }
}
