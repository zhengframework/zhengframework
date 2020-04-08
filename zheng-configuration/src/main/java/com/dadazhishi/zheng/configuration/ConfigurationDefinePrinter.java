package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;

public class ConfigurationDefinePrinter {

  private static JavaPropsMapper javaPropsMapper = new JavaPropsMapper();

  public static void print(Iterator<ConfigurationDefine> iterator, PrintWriter writer)
      throws IOException, NoSuchFieldException, IllegalAccessException {

    JavaPropsMapper.builder().build();
    while (iterator.hasNext()) {
      ConfigurationDefine define = iterator.next();
      Field prefix = define.getClass().getDeclaredField("PREFIX");
      Map<String, String> map = javaPropsMapper.writeValueAsMap(define);
      for (Entry<String, String> entry : map.entrySet()) {
        writer.print(prefix.get(define));
        writer.print(".");
        writer.print(entry.getKey());
        writer.print("=");
        writer.println(entry.getValue());
      }
    }
    writer.flush();
    writer.close();
  }

  public static void print(PrintWriter writer)
      throws IOException, NoSuchFieldException, IllegalAccessException {
    ServiceLoader<ConfigurationDefine> loader = ServiceLoader.load(ConfigurationDefine.class);
    Iterator<ConfigurationDefine> iterator = loader.iterator();
    print(iterator, writer);
  }

}
