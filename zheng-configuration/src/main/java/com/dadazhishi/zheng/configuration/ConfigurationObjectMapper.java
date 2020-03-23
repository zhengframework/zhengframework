package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.annotation.ConfigurationDefine;
import com.dadazhishi.zheng.configuration.annotation.ConfigurationType;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ConfigurationObjectMapper {

  private final ConfigurationMapper mapper;

  public ConfigurationObjectMapper(ConfigurationMapper mapper) {
    this.mapper = mapper;
  }

  public <T> T resolve(Configuration configuration, Class<T> aClass) {
    if (!aClass.isAnnotationPresent(ConfigurationDefine.class)) {
      throw new RuntimeException("class need with annotation @ConfigurationDefine");
    }
    ConfigurationDefine annotation = aClass.getAnnotation(ConfigurationDefine.class);
    if (annotation.type() == ConfigurationType.BEAN) {
      if (annotation.namespace().isEmpty()) {
        return mapper.resolve(configuration, aClass);
      } else {
        return mapper.resolve(configuration.getConfiguration(annotation.namespace()), aClass);
      }
    }
    throw new RuntimeException("invalid ConfigurationType " + annotation.type());
  }

  public <T> Set<T> resolveSet(Configuration configuration, Class<T> aClass) {
    if (!aClass.isAnnotationPresent(ConfigurationDefine.class)) {
      throw new RuntimeException("class need with annotation @ConfigurationDefine");
    }
    ConfigurationDefine annotation = aClass.getAnnotation(ConfigurationDefine.class);
    if (annotation.type() == ConfigurationType.SET) {
      if (annotation.namespace().isEmpty()) {
        throw new RuntimeException("ConfigurationType[SET] need namespace not empty");
      } else {
        Set<Configuration> configurationList = configuration
            .getConfigurationSet(annotation.namespace());
        Set<T> objects = new LinkedHashSet<>(configurationList.size());
        for (Configuration configuration1 : configurationList) {
          objects.add(mapper.resolve(configuration1, aClass));
        }
        return objects;
      }
    }
    throw new RuntimeException("invalid ConfigurationType " + annotation.type());
  }

  public <T> Map<String, T> resolveMap(Configuration configuration, Class<T> aClass) {
    if (!aClass.isAnnotationPresent(ConfigurationDefine.class)) {
      throw new RuntimeException("class need with annotation @ConfigurationDefine");
    }
    ConfigurationDefine annotation = aClass.getAnnotation(ConfigurationDefine.class);
    if (annotation.type() == ConfigurationType.MAP) {
      if (annotation.namespace().isEmpty()) {
        throw new RuntimeException("ConfigurationType[MAP] need namespace not empty");
      } else {
        Map<String, Configuration> configurationMap = configuration
            .getConfigurationMap(annotation.namespace());
        LinkedHashMap<String, T> map = new LinkedHashMap<>(configurationMap.size());
        for (Entry<String, Configuration> entry : configurationMap
            .entrySet()) {
          map.put(entry.getKey(), mapper.resolve(entry.getValue(), aClass));
        }
        return map;
      }
    }
    throw new RuntimeException("invalid ConfigurationType " + annotation.type());
  }
}
