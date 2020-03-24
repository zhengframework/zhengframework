package com.dadazhishi.zheng.configuration.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ProvideSystemProperty;

public class SystemPropertiesConfigurationResolverTest {

  @Rule
  public final ProvideSystemProperty myPropertyHasMyValue
      = new ProvideSystemProperty("test-abc",
      "SystemPropertiesConfigurationResolver");

  SystemPropertiesConfigurationResolver resolver;

  @Before
  public void setup() {
    resolver = new SystemPropertiesConfigurationResolver();
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