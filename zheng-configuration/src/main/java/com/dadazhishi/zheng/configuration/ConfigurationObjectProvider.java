package com.dadazhishi.zheng.configuration;

import javax.inject.Provider;

public class ConfigurationObjectProvider<T> implements Provider<T> {

  private final ConfigurationMapper mapper;
  private final Configuration configuration;
  private final Class<? extends T> aClass;

  public ConfigurationObjectProvider(ConfigurationMapper mapper,
      Configuration configuration, Class<? extends T> aClass) {
    this.mapper = mapper;
    this.configuration = configuration;
    this.aClass = aClass;
  }

  @Override
  public T get() {
    return mapper.resolve(configuration, aClass);
  }
}
