package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.dadazhishi.zheng.configuration.parser.PropertiesConfigurationParser;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationTest {

  private Map<String, String> map;

  @Before
  public void setUp() {
    PropertiesConfigurationParser parser = new PropertiesConfigurationParser();
    map = parser
        .parse(ConfigurationTest.class.getResourceAsStream("/food.properties"));
  }

  @Test
  public void testGet() {
    NamespaceConfiguration configuration = new NamespaceConfiguration(map);
    Configuration apple = configuration.getConfiguration("apple");
    assertTrue(Boolean.parseBoolean(apple.get("big").get()));
    assertEquals("110", apple.get("name").get());
    assertTrue(Double.parseDouble(apple.get("weight").get()) == 1.9);
  }

  @Test
  public void getList() {
    NamespaceConfiguration configuration = new NamespaceConfiguration(map);
    Set<Configuration> bananas = configuration.getConfigurationSet("bananas");
    assertEquals(3, bananas.size());

  }

  @Test
  public void getMap() {
    NamespaceConfiguration configuration = new NamespaceConfiguration(map);
    Map<String, Configuration> apples = configuration.getConfigurationMap("apples");
    assertEquals(1, apples.size());
  }
}