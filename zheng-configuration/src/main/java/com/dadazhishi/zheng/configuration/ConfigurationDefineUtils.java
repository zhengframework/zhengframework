package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.annotation.ConfigurationExample;
import com.dadazhishi.zheng.configuration.annotation.ConfigurationInfo;
import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

public class ConfigurationDefineUtils {

  private static JavaPropsMapper javaPropsMapper = new JavaPropsMapper();

  public static void print(Iterator<ConfigurationDefine> iterator, PrintWriter writer)
      throws Exception {
    while (iterator.hasNext()) {
      ConfigurationDefine define = iterator.next();
      Class<? extends ConfigurationDefine> aClass = define.getClass();
      checkConfigurationDefine(aClass);
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
          Map<String, String> map = javaPropsMapper
              .writeValueAsMap(example.example().newInstance());
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
        Map<String, String> map = javaPropsMapper.writeValueAsMap(define);
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
    writer.flush();
  }

  public static void checkConfigurationDefine(Class<?> aClass) {
    if (!aClass.isAnnotationPresent(ConfigurationInfo.class)) {
      throw new IllegalStateException(
          aClass.getName() + " is not annotation present @ConfigurationInfo");
    }
  }

  public static void print(PrintWriter writer)
      throws Exception {
    ServiceLoader<ConfigurationDefine> loader = ServiceLoader.load(ConfigurationDefine.class);
    Iterator<ConfigurationDefine> iterator = loader.iterator();
    print(iterator, writer);
  }

}
