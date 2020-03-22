package com.dadazhishi.zheng.configuration;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;

public class ConfigurationMapper {

  private final JavaPropsMapper mapper = new JavaPropsMapper();

  public <T> T resolve(Configuration configuration, Class<T> tClass) {
    try {
      return mapper.readMapAs(configuration, tClass);
    } catch (IOException e) {
      throw new RuntimeException("resolve configuration error", e);
    }
  }
}
