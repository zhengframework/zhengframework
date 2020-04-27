package com.github.zhengframework.test;

import static com.google.inject.name.Names.named;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ZhengApplicationRunner.class)
public class ZhengApplicationRunnerConfigFileTest {

  @Inject
  private Injector injector;

  @Test
  @WithZhengApplication()
  public void testDefault() {
    assertNotNull(injector);
    String url = injector
        .getInstance(Key.get(String.class, named("zheng.dataSource.jdbcUrl")));
    assertNotNull(url);
    assertEquals("jdbc:hsqldb:mem:test_db", url);
  }

  @Test
  @WithZhengApplication(configFile = "a.properties")
  public void testConfigA() {
    assertNotNull(injector);
    String url = injector
        .getInstance(Key.get(String.class, named("zheng.dataSource.jdbcUrl")));
    assertNotNull(url);
    assertEquals("a", url);
  }

  @Test
  @WithZhengApplication(configFile = "b.properties")
  public void testConfigB() {
    assertNotNull(injector);
    String url = injector
        .getInstance(Key.get(String.class, named("zheng.dataSource.jdbcUrl")));
    assertNotNull(url);
    assertEquals("b", url);
  }
}