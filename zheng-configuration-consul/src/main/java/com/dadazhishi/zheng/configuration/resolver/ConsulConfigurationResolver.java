package com.dadazhishi.zheng.configuration.resolver;

import com.orbitz.consul.Consul;
import com.orbitz.consul.Consul.Builder;
import com.orbitz.consul.KeyValueClient;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class ConsulConfigurationResolver implements ConfigurationResolver, AutoCloseable {

  private Consul client;
  private KeyValueClient keyValueClient;

  public ConsulConfigurationResolver() {
    Builder builder = Consul.builder();
    client = builder
        .withReadTimeoutMillis(3000)
        .build();
    keyValueClient = client.keyValueClient();
  }

  public ConsulConfigurationResolver(String url) {
    Builder builder = Consul.builder();
    if (StringUtils.isNotBlank(url)) {
      builder.withUrl(url);
    }
    client = builder
        .withReadTimeoutMillis(3000)
        .build();
    keyValueClient = client.keyValueClient();
  }

  public ConsulConfigurationResolver(Consul client) {
    this.client = client;
    keyValueClient = client.keyValueClient();
  }

  @Override
  public void close() {
    client.destroy();
  }

  @Override
  public Optional<String> get(String key) {
    return keyValueClient.getValueAsString(key);
  }

  @Override
  public Set<String> keySet() {
    return Collections.emptySet();
  }
}
