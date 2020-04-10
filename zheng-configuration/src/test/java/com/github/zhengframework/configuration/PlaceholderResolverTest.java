package com.github.zhengframework.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;

public class PlaceholderResolverTest {

  private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(\\$\\{(.*?)})");

  @Test
  public void resolvePlaceholders() {
    String value = "hello, ${name} ${${name}aa} ${key}";
    Matcher matcher = PLACEHOLDER_PATTERN.matcher(value);
    while (matcher.find()) {
      if (matcher.groupCount() == 2) {
        String placeholder = matcher.group(1);
        String property = matcher.group(2);
        System.out.println(placeholder);
        System.out.println(property);
        System.out.println(matcher.start(1) + ":" + matcher.end(1));
        System.out.println(matcher.start(2) + ":" + matcher.end(2));
      }

    }
  }
}