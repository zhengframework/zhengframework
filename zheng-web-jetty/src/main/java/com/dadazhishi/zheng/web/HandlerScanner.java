package com.dadazhishi.zheng.web;

import com.dadazhishi.zheng.service.Scanner;
import com.google.inject.Injector;
import javax.inject.Inject;
import org.eclipse.jetty.server.Handler;

/**
 * Walks through the guice injector bindings, visiting each one that is a Handler.
 */
public class HandlerScanner extends Scanner<Handler> {

  @Inject
  public HandlerScanner(Injector injector) {
    super(injector, Handler.class);
  }
}
