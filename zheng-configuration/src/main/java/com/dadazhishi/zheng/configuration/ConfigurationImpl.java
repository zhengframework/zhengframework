package com.dadazhishi.zheng.configuration;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class ConfigurationImpl extends AbstractMap<String, String> implements Configuration {

  private final PatriciaTrie<String> patriciaTrie;

  public ConfigurationImpl() {
    this.patriciaTrie = new PatriciaTrie<>();
  }

  public ConfigurationImpl(Map<String, String> map) {
    this.patriciaTrie = new PatriciaTrie<>(map);
  }

  public static ConfigurationImpl from(Map<String, String> map) {
    return new ConfigurationImpl(map);
  }

  @SuppressWarnings("NullableProblems")
  @Override
  public Set<Entry<String, String>> entrySet() {
    return patriciaTrie.entrySet();
  }

  @Override
  public Configuration prefix(String prefix) {
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix);
    return new ConfigurationImpl(prefixMap);
  }

  @Override
  public Configuration prefixWithoutHead(String prefix) {
    Objects.requireNonNull(prefix);
    int length = prefix.length();
    SortedMap<String, String> prefixMap = patriciaTrie.prefixMap(prefix);
    ConfigurationImpl configuration = new ConfigurationImpl();
    for (Entry<String, String> entry : prefixMap.entrySet()) {
      configuration.put(entry.getKey().substring(length), entry.getValue());
    }
    return configuration;
  }

}
