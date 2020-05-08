package com.github.zhengframework.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
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
        log.info("{}", placeholder);
        log.info("{}", property);
        log.info("{}", matcher.start(1) + ":" + matcher.end(1));
        log.info("{}", matcher.start(2) + ":" + matcher.end(2));
      }
    }
  }
}
