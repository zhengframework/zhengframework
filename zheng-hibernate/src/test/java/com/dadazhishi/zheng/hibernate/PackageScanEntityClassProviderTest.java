package com.dadazhishi.zheng.hibernate;

import static org.junit.Assert.assertTrue;

import java.util.List;
import org.junit.Test;

public class PackageScanEntityClassProviderTest {

  @Test
  public void get() {
    final PackageScanEntityClassProvider provider = new PackageScanEntityClassProvider(
        "com.dadazhishi.zheng.hibernate");
    final List<Class<?>> entities = provider.get();
    assertTrue(entities.size() > 0);
//    assertEquals(TestEntity.class, entities.get(0));
  }
}