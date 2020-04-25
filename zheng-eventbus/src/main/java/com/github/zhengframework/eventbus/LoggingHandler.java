package com.github.zhengframework.eventbus;

import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"UnstableApiUsage", "NullableProblems"})
public class LoggingHandler implements SubscriberExceptionHandler {

  private static Logger logger(SubscriberExceptionContext context) {
    return LoggerFactory
        .getLogger(LoggingHandler.class.getName() + "." + context.getEventBus().identifier());
  }

  @Override
  public void handleException(Throwable exception,
      SubscriberExceptionContext context) {
    Logger logger = logger(context);
    logger.debug(message(context), exception);
  }

  private String message(SubscriberExceptionContext context) {
    Method method = context.getSubscriberMethod();
    return "Exception thrown by subscriber method " + method.getName() + '(' + method
        .getParameterTypes()[0].getName() + ')' + " on subscriber " + context.getSubscriber()
        + " when dispatching event: " + context.getEvent();
  }
}
