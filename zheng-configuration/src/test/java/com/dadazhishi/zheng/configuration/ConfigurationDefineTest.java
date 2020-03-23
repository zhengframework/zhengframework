package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationDefineTest {

  private final JavaPropsMapper mapper = new JavaPropsMapper();
  Map<String, String> map;
  Food food;

  @Before
  public void setUp() throws Exception {

    food = new Food();
    Apple apple = new Apple();
    apple.setBig(true);
    apple.setWeight(1.9);
    apple.setName("110");
    food.setApple(apple);
    food.setApples(Collections.singletonMap("abc", apple));
    List<Banana> list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Banana banana = new Banana();
      banana.setName("name" + i);
      banana.setColor(i * 100);
      list.add(banana);
    }
    food.setBananas(list);

    map = mapper.writeValueAsMap(food);
//    Properties properties = mapper.writeValueAsProperties(food);
//    properties.list(System.out);
  }

  @Test
  public void testGet() {
    ConfigurationImpl configuration = new ConfigurationImpl(map);
    Configuration apple = configuration.getConfiguration("apple");
    assertTrue(Boolean.parseBoolean(apple.get("big")));
    assertEquals("110", apple.get("name"));
    assertTrue(Double.parseDouble(apple.get("weight")) == 1.9);
  }

  @Test
  public void getList() {
    ConfigurationImpl configuration = new ConfigurationImpl(map);
    Set<Configuration> bananas = configuration.getConfigurationSet("bananas");
    assertEquals(3, bananas.size());

  }

  @Test
  public void getMap() {
    ConfigurationImpl configuration = new ConfigurationImpl(map);
    Map<String, Configuration> apples = configuration.getConfigurationMap("apples");
    assertEquals(1, apples.size());
  }
}