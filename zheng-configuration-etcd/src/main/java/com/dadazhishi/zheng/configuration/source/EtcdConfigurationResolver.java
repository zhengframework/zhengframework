package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeoutException;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;

public class EtcdConfigurationResolver implements ConfigurationResolver {

  private EtcdClient etcdClient;

  public EtcdConfigurationResolver(String... urls) {
    URI[] uris = new URI[urls.length];
    for (int i = 0; i < urls.length; i++) {
      String url = urls[i];
      uris[i] = URI.create(url);
    }

    etcdClient = new EtcdClient(uris);
  }

  public EtcdConfigurationResolver() {
    etcdClient = new EtcdClient();
  }

  public EtcdConfigurationResolver(EtcdClient etcdClient) {
    this.etcdClient = etcdClient;
  }

  @Override
  public Optional<String> get(String key) {
    try {
      EtcdKeysResponse etcdKeysResponse = etcdClient.get(key).send().get();
      String value = etcdKeysResponse.getNode().getValue();
      return Optional.ofNullable(value);
    } catch (IOException | EtcdAuthenticationException | TimeoutException | EtcdException e) {
      if (e instanceof EtcdException) {
        EtcdException etcdCause = ((EtcdException) e);
        if (etcdCause.errorCode == 100) {
          return Optional.empty();
        }
      }
      throw new RuntimeException("get key from etcd fail, key=" + key, e);
    }
  }

  @Override
  public Set<String> keySet() {
    return Collections.emptySet();
  }
}
