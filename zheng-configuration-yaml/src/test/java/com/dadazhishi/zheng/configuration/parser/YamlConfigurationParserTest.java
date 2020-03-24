package com.dadazhishi.zheng.configuration.parser;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.resolver.ClassPathConfigurationResolver;
import org.junit.Test;

public class YamlConfigurationParserTest {

  @Test
  public void parse() {
    Configuration configuration = ConfigurationBuilder.create()
        .withResolver(
            new ClassPathConfigurationResolver(new YamlConfigurationParser(), "food.yaml"))
        .build();

    assertEquals(3, configuration.getConfigurationSet("bananas").size());


  }
}