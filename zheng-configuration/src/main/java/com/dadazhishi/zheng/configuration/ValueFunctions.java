package com.dadazhishi.zheng.configuration;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class ValueFunctions {

  private static final Function<String, Integer> STRING_TO_INTEGER = Integer::parseInt;
  private static final Function<String, Long> STRING_TO_LONG = Long::parseLong;
  private static final Function<String, Float> STRING_TO_FLOAT = Float::parseFloat;
  private static final Function<String, Double> STRING_TO_DOUBLE = Double::parseDouble;
  private static final Function<String, BigDecimal> STRING_TO_BIG_DECIMAL = BigDecimal::new;
  private static final Function<String, BigInteger> STRING_TO_BIG_INTEGER = BigInteger::new;
  private static final Set<String> TRUE = ImmutableSet.of("true", "yes", "on", "1");
  private static final Set<String> FALSE = ImmutableSet.of("false", "no", "off", "0");
  private static final Function<String, Boolean> STRING_TO_BOOLEAN = new Function<String, Boolean>() {
    @Override
    public Boolean apply(String value) {
      String lower = Strings.nullToEmpty(value).toLowerCase();
      if (TRUE.contains(lower)) {
        return true;
      } else if (FALSE.contains(lower)) {
        return false;
      }
      throw new IllegalStateException("Unparseable boolean value: " + value);
    }
  };

  public static Function<String, Integer> toInt() {
    return STRING_TO_INTEGER;
  }

  public static Function<String, Long> toLong() {
    return STRING_TO_LONG;
  }

  public static Function<String, Float> toFloat() {
    return STRING_TO_FLOAT;
  }

  public static Function<String, Double> toDouble() {
    return STRING_TO_DOUBLE;
  }

  public static Function<String, Boolean> toBoolean() {
    return STRING_TO_BOOLEAN;
  }

  public static Function<String, BigDecimal> toBigDecimal() {
    return STRING_TO_BIG_DECIMAL;
  }

  public static Function<String, BigInteger> toBigInteger() {
    return STRING_TO_BIG_INTEGER;
  }

  public static Function<String, File> toFile() {
    return File::new;
  }

  public static Function<String, Path> toPath() {
    return s -> Paths.get(s);
  }

  public static <T extends Enum<T>> Function<String, T> toEnum(Class<T> enumType) {
    return s -> Enum.valueOf(enumType, s);
  }

  public static <V, T> Optional<V> transform(Optional<T> optional, Function<T, V> transform) {
    return optional.map(transform);
  }

  public static Function<String, LocalTime> toLocalTime() {
    return LocalTime::parse;
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

  public static Function<String, LocalDateTime> toLocalDateTime() {
    return LocalDateTime::parse;
  }

  public static Function<String, LocalDate> toLocalDate() {
    return LocalDate::parse;
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
}
