package com.github.zhengframework.configuration;

import java.util.HashMap;
import java.util.List;
import org.junit.Test;

public class ConfigurationTest {

  @Test
  public void prefixSet() {
    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.hibernate.driverClassName", "org.h2.Driver");
    map.put("zheng.hibernate.url", "jdbc:h2:mem:test");
    map.put("zheng.hibernate.username", "sa");
    map.put("zheng.hibernate.userIds.1", "sa1");
    map.put("zheng.hibernate.userIds.2", "sa2");
    map.put("zheng.hibernate.userIds.3", "sa3");
    map.put("zheng.hibernate.userIds.4", "sa4");
    map.put("zheng.hibernate.entityPackages", "com.github.zhengframework.guice");
    map.put("zheng.hibernate.stringStringMap.hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    map.put("zheng.hibernate.stringStringMap.hibernate.hbm2ddl.auto", "create");

    Configuration configuration = new MapConfiguration(map);

    List<Configuration> prefixSet = configuration.prefixList("zheng.hibernate.userIds");
    for (Configuration configuration1 : prefixSet) {
      System.out.println(configuration1.asMap().toString());
    }
  }
}
