package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class JsonConfigurationSourceTest {

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

    JsonConfigurationSource configurationSource = new JsonConfigurationSource(
        JsonConfigurationSourceTest.class.getResourceAsStream("/food.json"));

    Configuration configuration = configurationSource.read();
    ConfigurationMapper mapper = new ConfigurationMapper();
    Food food2 = mapper.resolve(configuration, Food.class);
    assertEquals(food, food2);

  }
}