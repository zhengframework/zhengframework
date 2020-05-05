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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.trie.PatriciaTrie;

public abstract class AbstractConfiguration implements Configuration {

  public static void checkPrefix(String prefix) {
    if (prefix == null || prefix.length() == 0) {
      throw new RuntimeException("invalid prefix");
    }
  }

  @Override
  public Configuration prefix(String prefix) {
    checkPrefix(prefix);
    PatriciaTrie<String> patriciaTrie = new PatriciaTrie<>(asMap());
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    return new MapConfiguration(prefixMap.entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().substring(len), Entry::getValue)));
  }

  @Override
  public List<Configuration> prefixList(String prefix) {
    checkPrefix(prefix);
    PatriciaTrie<String> patriciaTrie = new PatriciaTrie<>(asMap());
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    Map<Integer, Map<String, String>> map = new HashMap<>();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      String key = entry.getKey().substring(len);
      if (key.contains(".")) {
        String indexKey = key.substring(0, key.indexOf("."));
        if (indexKey.length() == 0) {
          throw new RuntimeException("parse index error, key=" + key);
        }
        int index;
        try {
          index = Integer.parseInt(key.substring(0, key.indexOf(".")));
        } catch (NumberFormatException e) {
          throw new RuntimeException("parse index error, key=" + key);
        }
        String newKey = key.substring(key.indexOf(".") + 1);
        if (newKey.length() == 0) {
          throw new RuntimeException("parse configuration key error, key=" + key);
        }
        if (!map.containsKey(index)) {
          map.put(index, new HashMap<>());
        }
        map.get(index).put(newKey, entry.getValue());
      } else {
        int index;
        try {
          index = Integer.parseInt(key);
        } catch (NumberFormatException e) {
          throw new RuntimeException("parse index error, key=" + key);
        }
        String newKey = "";
        if (!map.containsKey(index)) {
          map.put(index, new HashMap<>());
        }
        map.get(index).put(newKey, entry.getValue());
      }

    }
    return map.entrySet().stream().sorted(Entry.comparingByKey())
        .map(entry -> new MapConfiguration(entry.getValue()))
        .collect(Collectors.toList());
  }

  @Override
  public Set<Configuration> prefixSet(String prefix) {
    checkPrefix(prefix);
    PatriciaTrie<String> patriciaTrie = new PatriciaTrie<>(asMap());
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    Map<Integer, Map<String, String>> map = new HashMap<>();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      String key = entry.getKey().substring(len);
      if (key.contains(".")) {
        String indexKey = key.substring(0, key.indexOf("."));
        if (indexKey.length() == 0) {
          throw new RuntimeException("parse index error, key=" + key);
        }
        int index;
        try {
          index = Integer.parseInt(key.substring(0, key.indexOf(".")));
        } catch (NumberFormatException e) {
          throw new RuntimeException("parse index error, key=" + key);
        }
        String newKey = key.substring(key.indexOf(".") + 1);
        if (newKey.length() == 0) {
          throw new RuntimeException("parse configuration key error, key=" + key);
        }
        if (!map.containsKey(index)) {
          map.put(index, new HashMap<>());
        }
        map.get(index).put(newKey, entry.getValue());
      } else {
        int index;
        try {
          index = Integer.parseInt(key);
        } catch (NumberFormatException e) {
          throw new RuntimeException("parse index error, key=" + key);
        }
        String newKey = "";
        if (!map.containsKey(index)) {
          map.put(index, new HashMap<>());
        }
        map.get(index).put(newKey, entry.getValue());
      }
    }
    return map.values().stream().map(MapConfiguration::new).collect(Collectors.toSet());
  }

  @Override
  public Map<String, Configuration> prefixMap(String prefix) {
    checkPrefix(prefix);
    PatriciaTrie<String> patriciaTrie = new PatriciaTrie<>(asMap());
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    Map<String, Map<String, String>> map = new HashMap<>();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      String key = entry.getKey().substring(len);
      if (!key.contains(".")) {
        continue;
      }
      String mapKey = key.substring(0, key.indexOf("."));
      if (mapKey.length() == 0) {
        throw new RuntimeException("parse map key error, key=" + key);
      }
      String newKey = key.substring(key.indexOf(".") + 1);
      if (newKey.length() == 0) {
        throw new RuntimeException("parse configuration key error, key=" + key);
      }
      if (!map.containsKey(mapKey)) {
        map.put(mapKey, new HashMap<>());
      }
      map.get(mapKey).put(newKey, entry.getValue());
    }
    return map.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey,
            entry -> new MapConfiguration(entry.getValue())));
  }
}
