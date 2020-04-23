package com.github.zhengframework.log;

import com.google.inject.AbstractModule;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class SLF4JBridgeModule extends AbstractModule {

  public SLF4JBridgeModule() {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();
  }

}
