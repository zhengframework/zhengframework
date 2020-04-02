package com.dadazhishi.zheng.configuration;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class ValueFunctions {

  private static final Set<String> TRUE = ImmutableSet.of("true", "yes", "on", "1");
  private static final Set<String> FALSE = ImmutableSet.of("false", "no", "off", "0");
  private static final Function<String, Boolean> STRING_TO_BOOLEAN = value -> {
    String lower = Strings.nullToEmpty(value).toLowerCase();
    if (TRUE.contains(lower)) {
      return true;
    } else if (FALSE.contains(lower)) {
      return false;
    }
    throw new IllegalStateException("cannot parse boolean value: " + value);
  };

  public static Function<String, Boolean> toBoolean() {
    return STRING_TO_BOOLEAN;
  }

  public static Function<String, Calendar> toCalendar() {
    return s -> {
      Calendar c = Calendar.getInstance();
      try {
        c.setTime(new Date(Long.parseLong(s)));
        return c;
      } catch (NumberFormatException ignored) {
      }
      try {
        c.setTime(new SimpleDateFormat().parse(s));
      } catch (ParseException e) {
        throw new RuntimeException("illegal value", e);
      }
      return c;
    };
  }

  public static Function<String, URI> toURI() {
    return s -> {
      try {
        return new URI(s);
      } catch (URISyntaxException e) {
        throw new RuntimeException("illegal value", e);
      }
    };
  }

  public static Function<String, URL> toURL() {
    return s -> {
      try {
        return new URL(s);
      } catch (MalformedURLException e) {
        throw new RuntimeException("illegal value", e);
      }
    };
  }

  public static Function<String, String[]> toArray(String separator) {
    return value -> value.split(separator);
  }

  public static Function<String, List<String>> toList(String separator) {
    return value -> Arrays.asList(value.split(separator));
  }

}