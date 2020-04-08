package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.source.FileConfigurationSource;
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