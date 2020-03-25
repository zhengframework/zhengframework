package com.dadazhishi.zheng.configuration;

import static java.util.Arrays.stream;

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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

  default String getString(String key, String defaultValue) {
    return getString(key).orElse(defaultValue);
  }

  default Optional<Integer> getInt(String key) {
    return getValue(key, ValueFunctions.toInt());
  }

  default Integer getInt(String key, Integer defaultValue) {
    return getInt(key).orElse(defaultValue);
  }

  default Optional<Long> getLong(String key) {
    return getValue(key, ValueFunctions.toLong());
  }

  default Long getLong(String key, Long defaultValue) {
    return getLong(key).orElse(defaultValue);
  }

  default Optional<Float> getFloat(String key) {
    return getValue(key, ValueFunctions.toFloat());
  }

  default Float getFloat(String key, Float defaultValue) {
    return getFloat(key).orElse(defaultValue);
  }

  default Optional<Double> getDouble(String key) {
    return getValue(key, ValueFunctions.toDouble());
  }

  default Double getDouble(String key, Double defaultValue) {
    return getDouble(key).orElse(defaultValue);
  }

  default Optional<Boolean> getBoolean(String key) {
    return getValue(key, ValueFunctions.toBoolean());
  }

  default Boolean getBoolean(String key, Boolean defaultValue) {
    return getBoolean(key).orElse(defaultValue);
  }

  default Optional<BigDecimal> getBigDecimal(String key) {
    return getValue(key, ValueFunctions.toBigDecimal());
  }

  default BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
    return getBigDecimal(key).orElse(defaultValue);
  }

  default Optional<BigInteger> getBigInteger(String key) {
    return getValue(key, ValueFunctions.toBigInteger());
  }

  default BigInteger getBigInteger(String key, BigInteger defaultValue) {
    return getBigInteger(key).orElse(defaultValue);
  }

  default Optional<File> getFile(String key) {
    return getValue(key, ValueFunctions.toFile());
  }

  default File getFile(String key, File defaultValue) {
    return getFile(key).orElse(defaultValue);
  }

  default Optional<Path> getPath(String key) {
    return getValue(key, ValueFunctions.toPath());
  }

  default Path getPath(String key, Path defaultValue) {
    return getPath(key).orElse(defaultValue);
  }

  default Optional<Calendar> getCalendar(String key) {
    return getValue(key, ValueFunctions.toCalendar());
  }

  default Calendar getCalendar(String key, Calendar defaultValue) {
    return getCalendar(key).orElse(defaultValue);
  }

  default Optional<LocalTime> getLocalTime(String key) {
    return getValue(key, ValueFunctions.toLocalTime());
  }

  default LocalTime getLocalTime(String key, LocalTime defaultValue) {
    return getLocalTime(key).orElse(defaultValue);
  }

  default Optional<LocalDateTime> getLocalDateTime(String key) {
    return getValue(key, ValueFunctions.toLocalDateTime());
  }

  default LocalDateTime getLocalDateTime(String key, LocalDateTime defaultValue) {
    return getLocalDateTime(key).orElse(defaultValue);
  }

  default Optional<LocalDate> getLocalDate(String key) {
    return getValue(key, ValueFunctions.toLocalDate());
  }

  default LocalDate getLocalDate(String key, LocalDate defaultValue) {
    return getLocalDate(key).orElse(defaultValue);
  }

  default Optional<URI> getURI(String key) {
    return getValue(key, ValueFunctions.toURI());
  }

  default URI getURI(String key, URI defaultValue) {
    return getURI(key).orElse(defaultValue);
  }

  default Optional<URL> getURL(String key) {
    return getValue(key, ValueFunctions.toURL());
  }

  default URL getURL(String key, URL defaultValue) {
    return getURL(key).orElse(defaultValue);
  }

  default <T extends Enum<T>> Optional<T> getEnum(String key, Class<T> enumType) {
    return getValue(key, ValueFunctions.toEnum(enumType));
  }

  default <T extends Enum<T>> T getEnum(String key, Class<T> enumType, T defaultValue) {
    return getEnum(key, enumType).orElse(defaultValue);
  }

  default <T> Optional<T> getValue(String key, Function<String, T> func) {
    return get(key).map(func);
  }

  default <T> T getValue(String key, Function<String, T> func, T defaultValue) {
    return get(key).map(func).orElse(defaultValue);
  }

  default Optional<String[]> getArray(String key) {
    return getArray(key, ",");
  }

  default String[] getArray(String key, String[] defaultValue) {
    return getArray(key, ",").orElse(defaultValue);
  }

  default Optional<String[]> getArray(String key, String separator) {
    return get(key).map(ValueFunctions.toArray(separator));
  }

  default String[] getArray(String key, String separator, String[] defaultValue) {
    return getArray(key, separator).orElse(defaultValue);
  }

  default List<String> getList(String key, String separator, List<String> defaultValue) {
    return get(key).map(ValueFunctions.toList(separator)).orElse(defaultValue);
  }

  default Optional<List<String>> getList(String key, String separator) {
    return get(key).map(ValueFunctions.toList(separator));
  }

  default Optional<List<String>> getList(String key) {
    return get(key).map(ValueFunctions.toList(","));
  }

  default List<String> getList(String key, List<String> defaultValue) {
    return get(key).map(ValueFunctions.toList(",")).orElse(defaultValue);
  }

  default <T> List<T> getList(String key, String separator, Function<String, T> map) {
    return getList(key, separator).orElse(Collections.emptyList()).stream().map(map)
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  default <T> T[] getArray(String key, String separator, Function<String, T> map) {
    return (T[]) stream(getArray(key, separator).orElse(new String[0]))
        .map(map).toArray();
  }

  default <T> List<T> getList(String key, Function<String, T> map) {
    return getList(key, ",").orElse(Collections.emptyList()).stream().map(map)
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  default <T> T[] getArray(String key, Function<String, T> map) {
    return (T[]) stream(getArray(key, ",").orElse(new String[0]))
        .map(map).toArray();
  }
}
