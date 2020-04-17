package com.github.zhengframework.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.zhengframework.configuration.parser.PropertiesConfigurationParser;
import java.util.List;
import java.util.Map;
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
    List<Configuration> bananas = configuration.prefixList("bananas");
    assertEquals(3, bananas.size());

  }

  @Test
  public void getMap() {
    MapConfiguration configuration = new MapConfiguration(map);
    Map<String, Configuration> apples = configuration.prefixMap("apples");
    assertEquals(1, apples.size());
  }
}