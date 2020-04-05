package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class ConfigurationImpl implements Configuration {

  private final ConfigurationResolver resolver;

  protected ConfigurationImpl(ConfigurationResolver resolver) {
    this.resolver = resolver;
  }

  public static ConfigurationBuilder builder() {
    return new ConfigurationBuilder();
  }

  static void checkPrefix(String prefix) {
    if (prefix == null || prefix.length() == 0) {
      throw new RuntimeException("invalid prefix");
    }
  }

  @Override
  public Optional<String> get(String key) {
    Optional<String> optional = resolver.get(key);
    if (optional.isPresent()) {
      PlaceHolder placeHolder = new PlaceHolder(this);
      return Optional.ofNullable(placeHolder.replace(optional.get()));
    } else {
      return optional;
    }
  }

  @Override
  public Set<String> keySet() {
    return resolver.keySet();
  }

  @Override
  public Map<String, String> asMap() {
    Map<String, String> map = Maps.newTreeMap();
    for (String key : resolver.keySet()) {
      map.put(key, get(key).orElse(null));
    }
    return Collections.unmodifiableMap(map);
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
        .map(entry -> new MapConfiguration(entry.getValue()))
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
            entry -> new MapConfiguration(entry.getValue())));
  }

}
