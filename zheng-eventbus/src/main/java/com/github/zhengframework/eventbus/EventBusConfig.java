package com.github.zhengframework.eventbus;

import com.github.zhengframework.configuration.ConfigurationDefine;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.google.common.eventbus.EventBus;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("UnstableApiUsage")
@ConfigurationInfo(prefix = "zheng.eventbus")
@NoArgsConstructor
@Data
public class EventBusConfig implements ConfigurationDefine {

  private boolean enable = true;
  private EventBusType type = EventBusType.SYNC;
  private String targetPackageName = null;
  private Class<? extends EventBus> eventBusClass;
}
