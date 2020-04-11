package com.github.zhengframework.configuration.parser;

import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import com.github.zhengframework.core.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class JsonConfigurationParserTest {

  @Test
  public void parse() {
    Configuration configuration = new ConfigurationBuilder()
        .withConfigurationSource(new FileConfigurationSource("food.json"))
        .build();
    Assert.assertEquals(3, configuration.prefixSet("bananas").size());
  }
}