package com.dadazhishi.zheng.configuration.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

public class EnvironmentConfigurationResolverTest {

  @ClassRule
  public static final EnvironmentVariables environmentVariables = new EnvironmentVariables()
      .set("test-abcd", "EnvironmentConfigurationResolver");
  EnvironmentConfigurationResolver resolver;

  @Before
  public void setup() {
    resolver = new EnvironmentConfigurationResolver();

  }

  @Test
  public void get() {
    assertEquals("EnvironmentConfigurationResolver",
        resolver.get("test.abcd").get());
  }

  @Test
  public void keySet() {
    assertTrue(resolver.keySet().isEmpty());
  }
}