package com.dadazhishi.zheng.configuration;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ConfigurationObjectMapper {

  private static final JavaPropsMapper mapper = new JavaPropsMapper();

  private static <T> T resolveClass(Configuration configuration, Class<T> tClass) {
    try {
      return mapper.readMapAs(configuration.asMap(), tClass);
    } catch (IOException e) {
      throw new RuntimeException("resolve configuration error", e);
    }
  }

  public static <T> T resolve(Configuration configuration, Class<T> aClass) {
    return resolve(configuration, null, aClass);
  }

  public static <T> T resolve(Configuration configuration, String namespace, Class<T> aClass) {
    if (Strings.isNullOrEmpty(namespace)) {
      return resolveClass(configuration, aClass);
    } else {
      return resolveClass(configuration.getConfiguration(namespace), aClass);
    }
  }

  public static <T> Set<T> resolveSet(Configuration configuration, String namespace,
      Class<T> aClass) {
    Preconditions.checkState(!Strings.isNullOrEmpty(namespace), "namespace cannot null or empty");
    Set<Configuration> configurationList = configuration
        .getConfigurationSet(namespace);
    Set<T> objects = new LinkedHashSet<>(configurationList.size());
    for (Configuration configuration1 : configurationList) {
      objects.add(resolveClass(configuration1, aClass));
    }
    return objects;
  }

  public static <T> Map<String, T> resolveMap(Configuration configuration, String namespace,
      Class<T> aClass) {
    Preconditions.checkState(!Strings.isNullOrEmpty(namespace), "namespace cannot null or empty");
    Map<String, Configuration> configurationMap = configuration
        .getConfigurationMap(namespace);
    LinkedHashMap<String, T> map = new LinkedHashMap<>(configurationMap.size());
    for (Entry<String, Configuration> entry : configurationMap
        .entrySet()) {
      map.put(entry.getKey(), resolveClass(entry.getValue(), aClass));
    }
    return map;
  }
}
