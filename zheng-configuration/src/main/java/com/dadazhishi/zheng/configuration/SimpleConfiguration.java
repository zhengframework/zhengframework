package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.environment.Environment;
import com.dadazhishi.zheng.configuration.source.ConfigurationSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class SimpleConfiguration implements Configuration {

  private final ConfigurationSource configurationSource;
  private final Environment environment;

  public SimpleConfiguration(
      ConfigurationSource configurationSource,
      Environment environment) {
    this.configurationSource = configurationSource;
    this.environment = environment;
  }

  public static void checkPrefix(String prefix) {
    if (prefix == null || prefix.length() == 0) {
      throw new RuntimeException("invalid prefix");
    }
  }

  @Override
  public Optional<String> get(String key) {
    Map<String, String> configuration = configurationSource.getConfiguration(environment);
    String value = configuration.get(key);
    if (value != null) {
      PlaceHolder placeHolder = new PlaceHolder(this);
      return Optional.ofNullable(placeHolder.replace(value));
    }
    return Optional.empty();
  }

  @Override
  public Set<String> keySet() {
    return configurationSource.getConfiguration(environment).keySet();
  }

  @Override
  public Map<String, String> asMap() {
    Map<String, String> configuration = configurationSource.getConfiguration(environment);
    Map<String, String> copy = new HashMap<>();
    PlaceHolder placeHolder = new PlaceHolder(this);
    for (Entry<String, String> entry : configuration.entrySet()) {
      copy.put(entry.getKey(), placeHolder.replace(entry.getValue()));
    }
    return copy;
  }

  @Override
  public Configuration prefix(String prefix) {
    checkPrefix(prefix);
    PatriciaTrie<String> patriciaTrie = new PatriciaTrie<>(asMap());
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix + ".");
    int len = prefix.length() + 1;
    return new SubsetConfiguration(prefixMap.entrySet().stream()
        .collect(Collectors.toMap(entry -> entry.getKey().substring(len), Entry::getValue)));
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
            entry -> new SubsetConfiguration(entry.getValue())));
  }
}
