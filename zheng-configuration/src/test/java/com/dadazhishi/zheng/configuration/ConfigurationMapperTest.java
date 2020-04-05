package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.objects.Food;
import java.io.IOException;
import org.junit.Test;

public class ConfigurationMapperTest {

  @Test
  public void resolve() throws IOException {
    Configuration configuration = ConfigurationBuilder.create()
        .withClassPathProperties("food.properties")
        .build();

    Food food = ConfigurationBeanMapper.resolve(configuration, Food.class);

    assertEquals(1, food.getApples().size());
    assertEquals(3, food.getBananas().size());

  }
}