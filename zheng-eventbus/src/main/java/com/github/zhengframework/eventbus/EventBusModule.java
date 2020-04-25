package com.github.zhengframework.eventbus;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.google.common.base.Strings;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import javax.inject.Singleton;

// code base on https://github.com/xvik/dropwizard-guicey-ext
@SuppressWarnings({"UnstableApiUsage", "rawtypes", "unchecked"})
public class EventBusModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, EventBusConfig> configMap = ConfigurationBeanMapper
        .resolve(getConfiguration(), EventBusConfig.class);
    EventBusConfig eventBusConfig = configMap.get("");
    bind(EventBusConfig.class).toInstance(eventBusConfig);
    if (eventBusConfig.isEnable()) {
      bind(EventSubscribersInfo.class).in(Singleton.class);
      EventBus eventBus = null;
      if (eventBusConfig.getType() == EventBusType.SYNC) {
        eventBus = new EventBus(new LoggingHandler());
      } else if (eventBusConfig.getType() == EventBusType.ASYNC) {
        eventBus = new AsyncEventBus(
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
            new LoggingHandler());
      } else if (eventBusConfig.getType() == EventBusType.CUSTOM) {
        try {
          eventBus = Objects.requireNonNull(eventBusConfig.getEventBusClass()).getConstructor()
              .newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      }
      bind(EventBus.class).toInstance(eventBus);
      Matcher matcher;
      if (!Strings.isNullOrEmpty(eventBusConfig.getTargetPackageName())) {
        matcher = Matchers.inSubpackage(eventBusConfig.getTargetPackageName());
      } else {
        matcher = Matchers.any();
      }
      bindListener(matcher, new EventBusTypeListener(eventBus));
    }

  }

}
