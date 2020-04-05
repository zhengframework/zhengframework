package com.dadazhishi.zheng.configuration.resolver;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CombinedConfigurationResolver implements ConfigurationResolver {

  private final List<ConfigurationResolver> resolvers = Collections
      .synchronizedList(Lists.newArrayList());

  public CombinedConfigurationResolver(List<ConfigurationResolver> resolvers) {
    this.resolvers.addAll(resolvers);
  }

  @Override
  public Optional<String> get(String key) {
    for (ConfigurationResolver resolver : resolvers) {
      Optional<String> value = resolver.get(key);
      if (value.isPresent()) {
        return value;
      }
    }
    return Optional.empty();
  }

  @Override
  public Set<String> keySet() {
    Set<String> keySet = Sets.newHashSet();
    for (ConfigurationResolver resolver : resolvers) {
      keySet.addAll(resolver.keySet());
    }
    return keySet;
  }

}
