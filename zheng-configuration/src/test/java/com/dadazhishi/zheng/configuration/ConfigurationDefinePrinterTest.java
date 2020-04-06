package com.dadazhishi.zheng.configuration;

import com.dadazhishi.zheng.configuration.spi.ConfigurationDefine;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class ConfigurationDefinePrinterTest {

  @Test
  public void print() throws IOException, NoSuchFieldException, IllegalAccessException {
    ConfigurationDefineExample defineExample = new ConfigurationDefineExample();
    defineExample.getIntegerList().add(1000);
    defineExample.getIntegerList().add(1011);
    defineExample.getProperties().put("aaa", "aaa");
    defineExample.getProperties().put("bbb", "bbb");
    List<ConfigurationDefine> list = Arrays.asList(defineExample);

    ConfigurationDefinePrinter.print(list.iterator(), new PrintWriter(System.out));
  }
}