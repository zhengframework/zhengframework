package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConfigurationMapperTest {

  @Test
  public void resolve() throws IOException {
    PropertiesConfigurationSource propertiesConfigurationSource = PropertiesConfigurationSource
        .load(PropertiesConfigurationSourceTest.class.getResourceAsStream("/food.properties"));

    Configuration configuration = propertiesConfigurationSource.read();
    ConfigurationMapper mapper = new ConfigurationMapper();

    Food food = mapper.resolve(configuration, Food.class);

    assertEquals(1, food.getApples().size());
    assertEquals(3, food.getBananas().size());

  }
}