package com.github.zhengframework.configuration;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.configuration.objects.Food;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

public class ConfigurationMapperTest {

  @Test
  public void resolve() throws IOException {

    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("food.properties"))
        .build();

    Food food = ConfigurationBeanMapper.resolve(configuration, null, Food.class);
    System.out.println(food);
    assertEquals(1, food.getApples().size());
    assertEquals(3, food.getBananas().size());

  }

  @Test
  public void resolveMap() throws IOException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("example.properties"))
        .build();
    Map<String, ConfigurationDefineExample> map = ConfigurationBeanMapper
        .resolve(configuration, ConfigurationDefineExample.class);
    ConfigurationDefineExample example = map.get("");
    System.out.println(example);
  }

  @Test
  public void resolveGroupMap() throws IOException {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("example_group.properties"))
        .build();
    Map<String, ConfigurationDefineExample> map = ConfigurationBeanMapper
        .resolve(configuration, ConfigurationDefineExample.class);

    for (Entry<String, ConfigurationDefineExample> entry : map
        .entrySet()) {
      System.out.println(entry.getKey());
      System.out.println(entry.getValue());
    }
  }
}