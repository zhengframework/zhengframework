package com.dadazhishi.zheng.configuration.resolver;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ReloadableConfigurationResolver implements ConfigurationResolver, Reloadable {

  private final AtomicReference<Map<String, String>> mapReference;

  public ReloadableConfigurationResolver() {
    mapReference = new AtomicReference<>();
    mapReference.set(Collections.emptyMap());
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(mapReference.get().get(key));
  }

  @Override
  public Set<String> keySet() {
    return mapReference.get().keySet();
  }

  protected void update(Map<String, String> newValue) {
    mapReference.set(newValue);
  }

}
