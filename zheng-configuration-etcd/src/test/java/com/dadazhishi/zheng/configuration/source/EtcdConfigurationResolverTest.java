package com.dadazhishi.zheng.configuration.source;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.Optional;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdKeysResponse.EtcdNode;
import org.junit.Test;

/**
 * require docker
 */
public class EtcdConfigurationResolverTest {

  @Test
  public void get() throws Exception {
    EtcdClient etcdClient = new EtcdClient(URI.create("http://localhost:32773"),
        URI.create("http://localhost:32771"));
    etcdClient.put("test_key", "test_value").send().get();
    EtcdKeysResponse etcdKeysResponse = etcdClient.get("test_key").send().get();
    System.out.println(etcdKeysResponse.getNode().getValue());
    EtcdKeysResponse response = etcdClient.getAll().send().get();
    for (EtcdNode node : response.getNode().getNodes()) {
      System.out.println(node.getKey());
      System.out.println(node.getValue());
    }
    etcdClient.close();
    EtcdConfigurationResolver resolver = new EtcdConfigurationResolver(
        "http://localhost:32773", "http://localhost:32771");
    Optional<String> a = resolver.get("a");
    assertFalse(a.isPresent());
    assertTrue(resolver.get("test_key").isPresent());
  }

  @Test
  public void keySet() {
  }
}