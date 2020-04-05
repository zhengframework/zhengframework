package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.objects.Apple;
import com.dadazhishi.zheng.configuration.objects.Banana;
import com.dadazhishi.zheng.configuration.objects.Food;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class JavaObjectConfigurationParserTest {

  @Test
  public void parse() {
    Food food;
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
    JavaObjectConfigurationParser parser = new JavaObjectConfigurationParser();
    Map<String, String> parse = parser.parse(food);

    Configuration configuration = ConfigurationBuilder.create()
        .withProperties("food.properties")
        .build();
    Assert.assertEquals(parse, configuration.asMap());

  }
}