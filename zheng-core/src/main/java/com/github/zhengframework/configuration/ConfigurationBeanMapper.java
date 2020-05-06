package com.github.zhengframework.configuration;

/*-
 * #%L
 * zheng-core
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static com.github.zhengframework.configuration.ConfigurationDefineUtils.checkConfigurationDefine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.github.drapostolos.typeparser.TypeParser;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

public class ConfigurationBeanMapper {

  private static final JavaPropsMapper mapper =
      JavaPropsMapper.builder()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
          .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
          .configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false)
          .configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false)
          .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
          .configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, false)
          .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
          .configure(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY, false)
          .configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true)
          .build();

  private static <T> T resolveClass(Configuration configuration, Class<T> tClass) {
    try {
      T t = mapper.readMapAs(configuration.asMap(), tClass);
      for (Field field : tClass.getDeclaredFields()) {
        if (field.isAnnotationPresent(JsonIgnore.class)) {
          field.setAccessible(true);
          Class<?> fieldType = field.getType();
          if (Map.class.isAssignableFrom(fieldType)) {
            Configuration prefix = configuration.prefix(field.getName());
            TypeParser parser = TypeParser.newBuilder().build();
            Type genericType = field.getGenericType();
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] typeArguments = parameterizedType.getActualTypeArguments();
            Type keyType = typeArguments[0];
            Type valueType = typeArguments[1];
            HashMap<Object, Object> map = new HashMap<>();
            for (Entry<String, String> entry : prefix.asMap().entrySet()) {
              map.put(
                  parser.parseType(entry.getKey(), keyType),
                  parser.parseType(entry.getValue(), valueType));
            }
            field.set(t, map);
          }
        }
      }
      return t;
    } catch (IOException | IllegalAccessException e) {
      throw new RuntimeException("resolve configuration error", e);
    }
  }

  public static <T> T resolve(Configuration configuration, String prefix, Class<T> aClass) {
    if (Strings.isNullOrEmpty(prefix)) {
      return resolveClass(configuration, aClass);
    } else {
      return resolveClass(configuration.prefix(prefix), aClass);
    }
  }

  public static <T> Set<T> resolveSet(Configuration configuration, String prefix, Class<T> aClass) {
    Preconditions.checkState(!Strings.isNullOrEmpty(prefix), "prefix cannot null or empty");
    List<Configuration> configurationList = configuration.prefixList(prefix);
    Set<T> objects = new LinkedHashSet<>(configurationList.size());
    for (Configuration configuration1 : configurationList) {
      objects.add(resolveClass(configuration1, aClass));
    }
    return objects;
  }

  public static <T> Map<String, T> resolveMap(
      Configuration configuration, String prefix, Class<T> aClass) {
    Preconditions.checkState(!Strings.isNullOrEmpty(prefix), "prefix cannot null or empty");
    Map<String, Configuration> configurationMap = configuration.prefixMap(prefix);
    LinkedHashMap<String, T> map = new LinkedHashMap<>(configurationMap.size());
    for (Entry<String, Configuration> entry : configurationMap.entrySet()) {
      map.put(entry.getKey(), resolveClass(entry.getValue(), aClass));
    }
    return map;
  }

  public static <C> Map<String, C> resolve(Configuration configuration, Class<? extends C> aClass) {
    checkConfigurationDefine(aClass);
    Map<String, C> map = new HashMap<>();
    ConfigurationInfo configurationInfo = aClass.getAnnotation(ConfigurationInfo.class);
    String prefix = configurationInfo.prefix();
    if (configurationInfo.supportGroup()) {
      Boolean group = configuration.getBoolean(prefix + ".group", false);
      if (!group) {
        C resolve = resolve(configuration, prefix, aClass);
        map.put("", resolve);
      } else {
        Map<String, ? extends C> resolveMap = resolveMap(configuration, prefix, aClass);
        resolveMap.forEach((BiConsumer<String, C>) map::put);
      }
    } else {
      C resolve = resolve(configuration, prefix, aClass);
      map.put("", resolve);
    }
    return Collections.unmodifiableMap(map);
  }

  public static <C> void resolve(
      Configuration configuration, Class<? extends C> aClass, BiConsumer<String, C> consumer) {
    checkConfigurationDefine(aClass);
    ConfigurationInfo configurationInfo = aClass.getAnnotation(ConfigurationInfo.class);
    String prefix = configurationInfo.prefix();
    if (configurationInfo.supportGroup()) {
      Boolean group = configuration.getBoolean(prefix + ".group", false);
      if (!group) {
        C resolve = resolve(configuration, prefix, aClass);
        consumer.accept("", resolve);
      } else {
        Map<String, ? extends C> resolveMap = resolveMap(configuration, prefix, aClass);
        resolveMap.forEach(consumer);
      }
    } else {
      C resolve = resolve(configuration, prefix, aClass);
      consumer.accept("", resolve);
    }
  }
}
