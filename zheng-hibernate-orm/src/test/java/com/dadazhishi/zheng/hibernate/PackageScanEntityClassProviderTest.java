package com.dadazhishi.zheng.hibernate;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

public class PackageScanEntityClassProviderTest {

  @Test
  public void get() {
    final PackageScanEntityClassProvider provider = new PackageScanEntityClassProvider(
        "com.dadazhishi.zheng.hibernate");
    final List<Class<?>> entities = provider.get();
    assertEquals(1, entities.size());
    assertEquals(TestEntity.class, entities.get(0));
  }
}