package com.dadazhishi.zheng.configuration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class PropertiesConfigurationSourceTest {

  @Test
  public void read() throws IOException {
    PropertiesConfigurationSource propertiesConfigurationSource = PropertiesConfigurationSource
        .load(PropertiesConfigurationSourceTest.class.getResourceAsStream("/food.properties"));

    Configuration configuration = propertiesConfigurationSource.read();
    Map<String, Configuration> apples = configuration.getConfigurationMap("apples");
    assertEquals(1, apples.size());
    List<Configuration> bananas = configuration.getConfigurationList("bananas");
    assertEquals(3, bananas.size());
  }
}