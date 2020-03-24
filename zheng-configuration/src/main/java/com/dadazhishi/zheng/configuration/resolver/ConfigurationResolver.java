package com.dadazhishi.zheng.configuration.resolver;

import java.util.Optional;
import java.util.Set;

public interface ConfigurationResolver {

  Optional<String> get(String key);

  Set<String> keySet();

}
