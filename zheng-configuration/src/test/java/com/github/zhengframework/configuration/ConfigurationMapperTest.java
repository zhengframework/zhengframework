package com.github.zhengframework.configuration;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.configuration.objects.Food;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.core.Configuration;
import java.io.IOException;
import org.junit.Test;

public class ConfigurationMapperTest {

  @Test
  public void resolve() throws IOException {

    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("food.properties"))
        .build();

    Food food = ConfigurationBeanMapper.resolve(configuration, Food.class);
    System.out.println(food);
    assertEquals(1, food.getApples().size());
    assertEquals(3, food.getBananas().size());

  }
}