package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.objects.Food;
import com.dadazhishi.zheng.configuration.source.FileConfigurationSource;
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