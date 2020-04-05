package com.dadazhishi.zheng.configuration.parser;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.resolver.ClasspathConfigurationResolver;
import org.junit.Assert;
import org.junit.Test;

public class JsonConfigurationParserTest {

  @Test
  public void parse() {
    FileLocator fileLocator = FileLocator.builder().fileName("food.json").build();
    Configuration configuration = ConfigurationBuilder.create()
        .withResolver(
            new ClasspathConfigurationResolver(fileLocator, new JsonConfigurationParser()))
        .build();
    Assert.assertEquals(3, configuration.prefixSet("bananas").size());
  }
}