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
    MapConfiguration configuration = new MapConfiguration(map);
    Configuration apple = configuration.prefix("apple");
    assertTrue(Boolean.parseBoolean(apple.get("big").get()));
    assertEquals("110", apple.get("name").get());
    assertTrue(Double.parseDouble(apple.get("weight").get()) == 1.9);
  }

  @Test
  public void getList() {
    MapConfiguration configuration = new MapConfiguration(map);
    Set<Configuration> bananas = configuration.prefixSet("bananas");
    assertEquals(3, bananas.size());

  }

  @Test
  public void getMap() {
    MapConfiguration configuration = new MapConfiguration(map);
    Map<String, Configuration> apples = configuration.prefixMap("apples");
    assertEquals(1, apples.size());
  }
}