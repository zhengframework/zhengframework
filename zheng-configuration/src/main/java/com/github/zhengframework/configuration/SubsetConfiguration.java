package com.github.zhengframework.configuration;


import static com.github.zhengframework.configuration.SimpleConfiguration.checkPrefix;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class SubsetConfiguration implements Configuration {

  private final PatriciaTrie<String> patriciaTrie;

  public SubsetConfiguration(Map<String, String> map) {
    this.patriciaTrie = new PatriciaTrie<>(map);
  }

  @Override
  public Optional<String> get(String key) {
    return Optional.ofNullable(patriciaTrie.get(key));
  }

  @Override
  public Set<String> keySet() {
    return patriciaTrie.keySet();
  }

  @Override
  public Map<String, String> asMap() {
    return patriciaTrie;
  }

  @Override
  public Configuration prefix(String prefix) {
    checkPrefix(prefix);
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    return new SubsetConfiguration(prefixMap.entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().substring(len), Entry::getValue)));
  }

  @Override
  public Set<Configuration> prefixSet(String prefix) {
    checkPrefix(prefix);
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    Map<Integer, Map<String, String>> map = new HashMap<>();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      String key = entry.getKey().substring(len);
      if (!key.contains(".")) {
        continue;
      }
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
        .map(entry -> new SubsetConfiguration(entry.getValue()))
        .collect(Collectors.toSet());
  }

  @Override
  public Map<String, Configuration> prefixMap(String prefix) {
    checkPrefix(prefix);
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
            entry -> new SubsetConfiguration(entry.getValue())));
  }
}
