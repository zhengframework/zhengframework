package com.dadazhishi.zheng.configuration.source;

import com.dadazhishi.zheng.configuration.resolver.ConfigurationResolver;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.curator.framework.CuratorFramework;

public class ZooKeeperConfigurationResolver implements ConfigurationResolver, AutoCloseable {

  private CuratorFramework curatorFramework;

  public ZooKeeperConfigurationResolver(
      CuratorFramework curatorFramework) throws InterruptedException {
    this.curatorFramework = curatorFramework;
    curatorFramework.start();
    curatorFramework.blockUntilConnected();
  }

  @Override
  public Optional<String> get(String key) {
    try {
      byte[] bytes = curatorFramework.getData().forPath("/" + key);
      if (bytes == null) {
        return Optional.empty();
      }
      return Optional.of(new String(bytes));
    } catch (Exception e) {
      throw new RuntimeException("get config from zookeeper fail, key=" + key, e);
    }
  }

  @Override
  public Set<String> keySet() {
    try {
      List<String> strings = curatorFramework.getChildren().forPath("/");
      return new HashSet<>(strings);
    } catch (Exception e) {
      throw new RuntimeException("get all config key from zookeeper fail", e);
    }
  }

  @Override
  public void close() {
    curatorFramework.close();
  }
}
