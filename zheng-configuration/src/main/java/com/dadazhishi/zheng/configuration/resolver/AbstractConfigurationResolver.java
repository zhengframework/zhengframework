package com.dadazhishi.zheng.configuration.resolver;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractConfigurationResolver implements ConfigurationResolver {

  protected abstract Map<String, String> delegate();

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(delegate().get(key));
  }

  @Override
  public Set<String> keySet() {
    return delegate().keySet();
  }
}
