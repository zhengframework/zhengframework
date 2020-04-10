package com.github.zhengframework.configuration.source;

import com.github.zhengframework.configuration.environment.Environment;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractConfigurationSource implements ConfigurationSource {

  private Map<String, Map<String, String>> oldConfig = new HashMap<>();
  private List<ConfigurationSourceListener> listenerList = new ArrayList<>();

  @Override
  public void addListener(ConfigurationSourceListener listener) {
    listenerList.add(Objects.requireNonNull(listener));
  }

  @Override
  public void removeListener(ConfigurationSourceListener listener) {
    listenerList.remove(Objects.requireNonNull(listener));
  }

  private void fireEvent(String environmentName, Map<String, String> newConfig) {
    Map<String, String> map = oldConfig.getOrDefault(environmentName, Collections.emptyMap());
    MapDifference<String, String> difference = Maps.difference(map, newConfig);
    oldConfig.put(environmentName, newConfig);
    if (!difference.areEqual()) {
      for (Entry<String, ValueDifference<String>> entry : difference
          .entriesDiffering().entrySet()) {
        for (ConfigurationSourceListener listener : listenerList) {
          if (listener.accept(entry.getKey())) {
            ValueDifference<String> valueDifference = entry.getValue();
            listener.onChanged(entry.getKey(), valueDifference.leftValue(),
                valueDifference.rightValue());
          }
        }
      }
    }
  }

  protected abstract Map<String, String> getConfigurationInternal(Environment environment);

  @Override
  public Map<String, String> getConfiguration(Environment environment) {
    final Map<String, String> newConfig = getConfigurationInternal(environment);
    fireEvent(StringUtils.trimToEmpty(environment.getName()), newConfig);
    return newConfig;
  }
}
