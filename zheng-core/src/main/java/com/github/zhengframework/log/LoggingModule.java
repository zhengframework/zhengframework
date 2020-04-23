package com.github.zhengframework.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import com.google.inject.AbstractModule;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class LoggingModule extends AbstractModule {

  public LoggingModule() {
    SLF4JBridgeHandler.removeHandlersForRootLogger();
    SLF4JBridgeHandler.install();

    final Logger root = (Logger) LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

    final LevelChangePropagator propagator = new LevelChangePropagator();
    propagator.setContext(root.getLoggerContext());
    propagator.setResetJUL(true);

    root.getLoggerContext().addListener(propagator);
  }

}
