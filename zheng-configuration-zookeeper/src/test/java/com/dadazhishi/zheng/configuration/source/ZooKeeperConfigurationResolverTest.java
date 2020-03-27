package com.dadazhishi.zheng.configuration.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ZooKeeperConfigurationResolverTest {

  private TestingServer testingServer;

  @Before
  public void setup() throws Exception {
    testingServer = new TestingServer();
  }

  @After
  public void close() throws IOException {
    testingServer.close();
  }


  @Test
  public void get() throws Exception {
    System.out.println(testingServer.getConnectString());
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
        .connectString(testingServer.getConnectString())
        .retryPolicy(retryPolicy)
        .build();
    ZooKeeperConfigurationResolver resolver = new ZooKeeperConfigurationResolver(
        curatorFramework);
    String value = "test value";
    for (int i = 0; i < 100; i++) {
      curatorFramework.create().forPath("/my.path" + i, value.getBytes());
    }

    Optional<String> stringOptional = resolver.get("my.path1");
    assertTrue(stringOptional.isPresent());
    assertEquals(value, stringOptional.get());

    List<String> strings = curatorFramework.getChildren().forPath("/");

    assertEquals(strings.size(), resolver.keySet().size());
    curatorFramework.close();
  }

}