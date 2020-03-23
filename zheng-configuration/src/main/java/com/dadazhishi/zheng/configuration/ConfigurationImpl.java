package com.dadazhishi.zheng.configuration;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class ConfigurationImpl extends AbstractMap<String, String> implements Configuration {

  private final PatriciaTrie<String> patriciaTrie;

  public ConfigurationImpl() {
    this.patriciaTrie = new PatriciaTrie<>();
  }

  public ConfigurationImpl(Map<String, String> map) {
    this.patriciaTrie = new PatriciaTrie<>(map);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public Set<Entry<String, String>> entrySet() {
    return patriciaTrie.entrySet();
  }

  public Configuration getConfiguration(String namespace) {
    if (namespace == null || namespace.length() == 0) {
      throw new RuntimeException("invalid namespace");
    }
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(namespace + ".");
    int len = namespace.length() + 1;
    return new ConfigurationImpl(prefixMap.entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().substring(len), Entry::getValue)));
  }

  @Override
  public Set<Configuration> getConfigurationSet(String namespace) {
    if (namespace == null || namespace.length() == 0) {
      throw new RuntimeException("invalid namespace");
    }
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(namespace + ".");
    int len = namespace.length() + 1;
    Map<Integer, Map<String, String>> map = new HashMap<>();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      String key = entry.getKey().substring(len);
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
    }
    return map.entrySet().stream().sorted(Entry.comparingByKey())
        .map(entry -> new ConfigurationImpl(entry.getValue()))
        .collect(Collectors.toSet());
  }

  @Override
  public Map<String, Configuration> getConfigurationMap(String namespace) {
    if (namespace == null || namespace.length() == 0) {
      throw new RuntimeException("invalid namespace");
    }
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(namespace + ".");
    int len = namespace.length() + 1;
    Map<String, Map<String, String>> map = new HashMap<>();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      String key = entry.getKey().substring(len);
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
            entry -> new ConfigurationImpl(entry.getValue())));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ConfigurationImpl that = (ConfigurationImpl) o;
    return patriciaTrie.equals(that.patriciaTrie);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), patriciaTrie);
  }

  public String put(String key, String value) {
    return patriciaTrie.put(key, value);
  }
}
