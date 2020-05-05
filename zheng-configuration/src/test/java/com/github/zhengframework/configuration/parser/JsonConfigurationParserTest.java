package com.github.zhengframework.configuration.parser;

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.configuration.ConfigurationBuilder;
import com.github.zhengframework.configuration.source.FileConfigurationSource;
import org.junit.Assert;
import org.junit.Test;

public class JsonConfigurationParserTest {

  @Test
  public void parse() {
    Configuration configuration =
        new ConfigurationBuilder()
            .withConfigurationSource(new FileConfigurationSource("food.json"))
            .build();
    Assert.assertEquals(3, configuration.prefixList("bananas").size());
  }
}
