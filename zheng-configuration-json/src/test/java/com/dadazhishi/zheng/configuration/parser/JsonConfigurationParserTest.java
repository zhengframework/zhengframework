package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.resolver.ClassPathConfigurationResolver;
import org.junit.Assert;
import org.junit.Test;

public class JsonConfigurationParserTest {

  @Test
  public void parse() {
    Configuration configuration = ConfigurationBuilder.create()
        .withResolver(
            new ClassPathConfigurationResolver(new JsonConfigurationParser(), "food.json"))
        .build();

    Assert.assertEquals(3, configuration.getConfigurationSet("bananas").size());

  }
}