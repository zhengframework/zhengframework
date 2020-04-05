package com.dadazhishi.zheng.configuration.parser;

import static org.junit.Assert.assertEquals;

import com.dadazhishi.zheng.configuration.Configuration;
import com.dadazhishi.zheng.configuration.ConfigurationBuilder;
import com.dadazhishi.zheng.configuration.io.FileLocator;
import com.dadazhishi.zheng.configuration.resolver.ClasspathConfigurationResolver;
import org.junit.Test;

public class YamlConfigurationParserTest {

  @Test
  public void parse() {
    FileLocator fileLocator = FileLocator.builder().fileName("food.yaml").build();
    Configuration configuration = ConfigurationBuilder.create()
        .withResolver(
            new ClasspathConfigurationResolver(fileLocator, new YamlConfigurationParser()))
        .build();

//    Configuration configuration = ConfigurationBuilder.create()
//        .withResolver(
//            new ClassPathConfigurationResolver(new YamlConfigurationParser(), "food.yaml"))
//        .build();

    assertEquals(3, configuration.prefixSet("bananas").size());


  }
}