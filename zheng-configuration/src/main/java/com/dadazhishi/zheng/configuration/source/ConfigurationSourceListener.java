package com.github.zhengframework.configuration.source;

import java.util.EventListener;
import javax.annotation.Nullable;

public interface ConfigurationSourceListener extends EventListener {

  boolean accept(String key);

  void onChanged(String key, @Nullable String oldValue, @Nullable String newValue);

}
