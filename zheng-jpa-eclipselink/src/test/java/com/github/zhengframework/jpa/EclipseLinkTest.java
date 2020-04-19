package com.github.zhengframework.jpa;

import com.github.zhengframework.jpa.a.Work;
import com.github.zhengframework.jpa.b.Work2;
import java.util.HashMap;
import org.junit.Test;

public class EclipseLinkTest {

  @Test
  public void test() throws Exception {
    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.jpa.driverClassName", "org.h2.Driver");
    map.put("zheng.jpa.url", "jdbc:h2:mem:test");
    map.put("zheng.jpa.username", "sa");
    map.put("zheng.jpa.managedClassPackages", "com.github.zhengframework.jpa.a");
    map.put("zheng.jpa.properties.eclipselink_ddl-generation", "create-tables");
    JpaModuleTest.test(map);
  }

  @Test
  public void testGroup() throws Exception {
    HashMap<String, String> map = new HashMap<>();
    map.put("zheng.jpa.group", "true");

    map.put("zheng.jpa.a.driverClassName", "org.h2.Driver");
    map.put("zheng.jpa.a.url", "jdbc:h2:mem:test_a");
    map.put("zheng.jpa.a.username", "sa");
    map.put("zheng.jpa.a.managedClassPackages", "com.github.zhengframework.jpa.a");
    map.put("zheng.jpa.a.properties.eclipselink_ddl-generation", "create-tables");
    map.put("zheng.jpa.a.exposeClassSet.1", Work.class.getName());

    map.put("zheng.jpa.b.driverClassName", "org.h2.Driver");
    map.put("zheng.jpa.b.url", "jdbc:h2:mem:test_b");
    map.put("zheng.jpa.b.username", "sa");
    map.put("zheng.jpa.b.managedClassPackages", "com.github.zhengframework.jpa.b");
    map.put("zheng.jpa.b.properties.eclipselink_ddl-generation", "create-tables");
    map.put("zheng.jpa.b.exposeClassSet.1", Work2.class.getName());
    JpaMultiModuleTest.test(map);
  }
}