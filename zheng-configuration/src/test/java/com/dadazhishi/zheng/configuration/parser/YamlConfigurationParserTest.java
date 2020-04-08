package com.dadazhishi.zheng.configuration.parser;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.source.FileConfigurationSource;
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