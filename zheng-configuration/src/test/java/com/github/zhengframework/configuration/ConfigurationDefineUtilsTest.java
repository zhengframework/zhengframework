package com.github.zhengframework.configuration;

import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import com.github.zhengframework.common.SuppressForbidden;
import com.github.zhengframework.configuration.annotation.ConfigurationExample;
import com.github.zhengframework.configuration.annotation.ConfigurationInfo;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import org.junit.Test;

public class ConfigurationDefineUtilsTest {

  private static JavaPropsMapper javaPropsMapper = new JavaPropsMapper();

  @SuppressForbidden
  @Test
  public void print() throws Exception {
    PrintWriter writer = new PrintWriter(System.out);
    Class<? extends ConfigurationDefine> aClass = ConfigurationDefineExample.class;

    if (!aClass.isAnnotationPresent(ConfigurationInfo.class)) {
      throw new IllegalStateException(
          aClass.getName() + " is not annotation present @ConfigurationInfo");
    }
    ConfigurationInfo configurationInfo = aClass.getAnnotation(ConfigurationInfo.class);
    String prefix = configurationInfo.prefix();
    if (configurationInfo.examples().length > 0) {
      if (Arrays.stream(configurationInfo.examples())
          .anyMatch(example -> !example.groupName().isEmpty())) {
        writer.print(prefix);
        writer.print(".");
        writer.println("group=true");
      }
      for (ConfigurationExample example : configurationInfo.examples()) {
        Map<String, String> map = javaPropsMapper.writeValueAsMap(example.example().newInstance());
        for (Entry<String, String> entry : map.entrySet()) {
          if (!prefix.isEmpty()) {
            writer.print(prefix);
            writer.print(".");
          }
          if (!example.groupName().isEmpty()) {
            writer.print(example.groupName());
            writer.print(".");
          }
          writer.print(entry.getKey());
          writer.print("=");
          writer.println(entry.getValue());
        }
      }
    } else {
      Map<String, String> map = javaPropsMapper.writeValueAsMap(aClass.newInstance());
      for (Entry<String, String> entry : map.entrySet()) {
        if (!prefix.isEmpty()) {
          writer.print(prefix);
          writer.print(".");
        }
        writer.print(entry.getKey());
        writer.print("=");
        writer.println(entry.getValue());
      }
    }
    writer.flush();
  }
}
