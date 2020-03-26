package com.dadazhishi.zheng.configuration.resolver;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.pszymczyk.consul.ConsulProcess;
import com.pszymczyk.consul.ConsulStarterBuilder;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConsulConfigurationResolverTest {

  String url;
  private ConsulProcess consul;

  @Before
  public void setup() {
    consul = ConsulStarterBuilder.consulStarter()
        .build().start();
    url = "http://localhost:" + consul.getHttpPort();
    Consul consulClient = Consul.builder().withUrl(url).build();
    KeyValueClient keyValueClient = consulClient.keyValueClient();
    keyValueClient.putValue("abc", "def");
  }

  @After
  public void cleanup() throws Exception {
    consul.close();
  }

  @Test
  public void get() {
    ConsulConfigurationResolver resolver
        = new ConsulConfigurationResolver(url);
    Optional<String> abc = resolver.get("abc");
    assertTrue(abc.isPresent());
    System.out.println(abc.get());
    assertFalse(resolver.get("abcd").isPresent());
  }


}