package com.dadazhishi.zheng.configuration.resolver;

import java.util.Optional;
import java.util.Set;

public class FallbackConfigurationResolver implements ConfigurationResolver {

  private final ConfigurationResolver delegate;

  private final ConfigurationResolver fallback;

  public FallbackConfigurationResolver(
      ConfigurationResolver delegate,
      ConfigurationResolver fallback) {
    this.delegate = delegate;
    this.fallback = fallback;
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(delegate.get(key)
        .orElse(fallback.get(key).orElse(null)));
  }

  @Override
  public Set<String> keySet() {
    return delegate.keySet();
  }

}
