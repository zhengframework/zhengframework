package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

public class PropertiesConfigurationSourceTest {

  @Test
  public void read() throws IOException {
    Food food = new Food();
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

    PropertiesConfigurationSource propertiesConfigurationSource =new PropertiesConfigurationSource
        (PropertiesConfigurationSourceTest.class.getResourceAsStream("/food.properties"));

    Configuration configuration = propertiesConfigurationSource.getConfiguration();
    Map<String, Configuration> apples = configuration.getConfigurationMap("apples");
    assertEquals(1, apples.size());
    Set<Configuration> bananas = configuration.getConfigurationSet("bananas");
    assertEquals(3, bananas.size());
    ConfigurationMapper mapper = new ConfigurationMapper();
    Food food2 = mapper.resolve(configuration, Food.class);
    assertEquals(food, food2);
  }
}