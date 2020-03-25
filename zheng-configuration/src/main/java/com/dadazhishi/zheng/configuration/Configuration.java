package com.dadazhishi.zheng.configuration;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface Configuration {

  Optional<String> get(String key);

  Set<String> keySet();

  Map<String, String> asMap();

  /**
   * 根据namespace获得静态Configuration实例
   */
  Configuration getConfiguration(String namespace);

  /**
   * 根据namespace获得静态Configuration实例
   */
  Set<Configuration> getConfigurationSet(String namespace);

  /**
   * 根据namespace获得静态Configuration实例
   */
  Map<String, Configuration> getConfigurationMap(String namespace);

  default Optional<String> getString(String key) {
    return get(key);
  }

  default Optional<Integer> getInt(String key) {
    return getValue(key, ValueFunctions.toInt());
  }

  default Optional<Long> getLong(String key) {
    return getValue(key, ValueFunctions.toLong());
  }

  default Optional<Float> getFloat(String key) {
    return getValue(key, ValueFunctions.toFloat());
  }

  default Optional<Double> getDouble(String key) {
    return getValue(key, ValueFunctions.toDouble());
  }

  default Optional<Boolean> getBoolean(String key) {
    return getValue(key, ValueFunctions.toBoolean());
  }

  default Optional<BigDecimal> getBigDecimal(String key) {
    return getValue(key, ValueFunctions.toBigDecimal());
  }

  default Optional<BigInteger> getBigInteger(String key) {
    return getValue(key, ValueFunctions.toBigInteger());
  }

  default Optional<File> toFile(String key) {
    return getValue(key, ValueFunctions.toFile());
  }

  default Optional<Path> toPath(String key) {
    return getValue(key, ValueFunctions.toPath());
  }

  default Optional<Calendar> toCalendar(String key) {
    return getValue(key, ValueFunctions.toCalendar());
  }

  default Optional<LocalTime> toLocalTime(String key) {
    return getValue(key, ValueFunctions.toLocalTime());
  }

  default Optional<LocalDateTime> toLocalDateTime(String key) {
    return getValue(key, ValueFunctions.toLocalDateTime());
  }

  default Optional<LocalDate> toLocalDate(String key) {
    return getValue(key, ValueFunctions.toLocalDate());
  }

  default Optional<URI> toURI(String key) {
    return getValue(key, ValueFunctions.toURI());
  }

  default Optional<URL> toURL(String key) {
    return getValue(key, ValueFunctions.toURL());
  }

  default <T extends Enum<T>> Optional<T> getEnum(String key, Class<T> enumType) {
    return getValue(key, ValueFunctions.toEnum(enumType));
  }

  default <T> Optional<T> getValue(String key, Function<String, T> func) {
    return ValueFunctions.transform(get(key), func);
  }

}
