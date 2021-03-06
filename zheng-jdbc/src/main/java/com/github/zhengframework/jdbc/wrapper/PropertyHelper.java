package com.github.zhengframework.jdbc.wrapper;

/*-
 * #%L
 * zheng-jdbc
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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that reflectively sets bean properties on a target object.
 *
 * @author Brett Wooldridge
 */
public final class PropertyHelper {

  private PropertyHelper() {
    // cannot be constructed
  }

  public static void setTargetFromProperties(
      final Object target, final Map<String, String> properties) {
    if (target == null || properties == null) {
      return;
    }

    List<Method> methods = Arrays.asList(target.getClass().getMethods());
    properties.forEach(
        (key, value) -> {
          setProperty(target, key, value, methods);
        });
  }

  private static void setProperty(
      final Object target,
      final String propName,
      final Object propValue,
      final List<Method> methods) {
    final Logger logger = LoggerFactory.getLogger(com.zaxxer.hikari.util.PropertyElf.class);

    // use the english locale to avoid the infamous turkish locale bug
    String methodName =
        "set" + propName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propName.substring(1);
    Method writeMethod =
        methods.stream()
            .filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1)
            .findFirst()
            .orElse(null);

    if (writeMethod == null) {
      String methodName2 = "set" + propName.toUpperCase(Locale.ENGLISH);
      writeMethod =
          methods.stream()
              .filter(m -> m.getName().equals(methodName2) && m.getParameterCount() == 1)
              .findFirst()
              .orElse(null);
    }

    if (writeMethod == null) {
      logger.error("Property {} does not exist on target {}", propName, target.getClass());
      throw new RuntimeException(
          String.format(
              Locale.ENGLISH,
              "Property %s does not exist on target %s",
              propName,
              target.getClass()));
    }

    try {
      Class<?> paramClass = writeMethod.getParameterTypes()[0];
      if (paramClass == int.class) {
        writeMethod.invoke(target, Integer.parseInt(propValue.toString()));
      } else if (paramClass == long.class) {
        writeMethod.invoke(target, Long.parseLong(propValue.toString()));
      } else if (paramClass == boolean.class || paramClass == Boolean.class) {
        writeMethod.invoke(target, Boolean.parseBoolean(propValue.toString()));
      } else if (paramClass == String.class) {
        writeMethod.invoke(target, propValue.toString());
      } else {
        try {
          logger.debug("Try to create a new instance of \"{}\"", propValue.toString());
          writeMethod.invoke(target, Class.forName(propValue.toString()).newInstance());
        } catch (InstantiationException | ClassNotFoundException e) {
          logger.debug(
              "Class \"{}\" not found or could not instantiate it (Default constructor)",
              propValue.toString());
          writeMethod.invoke(target, propValue);
        }
      }
    } catch (Exception e) {
      logger.error("Failed to set property {} on target {}", propName, target.getClass(), e);
      throw new RuntimeException(e);
    }
  }
}
