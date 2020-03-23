package com.dadazhishi.zheng.configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigurationUtils {

  @SuppressWarnings("unchecked")
  Map<String, Object> flatten(Map<String, Object> source) {
    Map<String, Object> result = new LinkedHashMap<>();

    for (String key : source.keySet()) {
      Object value = source.get(key);

      if (value instanceof Map) {
        Map<String, Object> subMap = flatten((Map<String, Object>) value);

        for (String subkey : subMap.keySet()) {
          result.put(key + "." + subkey, subMap.get(subkey));
        }
      } else if (value instanceof Collection) {
        StringBuilder joiner = new StringBuilder();
        String separator = "";

        for (Object element : ((Collection) value)) {
          Map<String, Object> subMap = flatten(Collections.singletonMap(key, element));
          joiner
              .append(separator)
              .append(subMap.entrySet().iterator().next().getValue().toString());

          separator = ",";
        }

        result.put(key, joiner.toString());
      } else {
        result.put(key, value);
      }
    }

    return result;
  }

}
