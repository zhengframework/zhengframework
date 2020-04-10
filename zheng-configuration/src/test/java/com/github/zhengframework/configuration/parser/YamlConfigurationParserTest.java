package com.github.zhengframework.configuration.parser;

import static org.junit.Assert.assertEquals;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import org.junit.Test;

public class YamlConfigurationParserTest {

  @Test
  public void parse() {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("food.yaml"))
        .build();

    assertEquals(3, configuration.prefixSet("bananas").size());


  }
}