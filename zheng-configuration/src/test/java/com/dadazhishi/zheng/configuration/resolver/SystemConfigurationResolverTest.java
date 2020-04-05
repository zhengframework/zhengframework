package com.dadazhishi.zheng.configuration.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;

public class SystemConfigurationResolverTest {

  @Rule
  public final ProvideSystemProperty myPropertyHasMyValue
      = new ProvideSystemProperty("test-abc",
      "SystemPropertiesConfigurationResolver");

  SystemConfigurationResolver resolver;

  @Before
  public void setup() {
    resolver = new SystemConfigurationResolver();
  }

  @Test
  public void keySet() {
    assertTrue(resolver.keySet().isEmpty());
  }

  @Test
  public void get() {
    assertEquals("SystemPropertiesConfigurationResolver", resolver.get("test-abc").get());
  }
}