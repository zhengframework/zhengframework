package com.github.zhengframework.configuration;

/*-
 * #%L
 * zheng-core
 * %%
 * Copyright (C) 2020 Zheng MingHai
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import static java.util.Arrays.stream;

import com.github.zhengframework.configuration.parser.Parser;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
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
   * 根据prefix获得静态Configuration实例
   *
   * @param prefix String
   * @return Configuration
   */
  Configuration prefix(String prefix);

  /**
   * 根据prefix获得静态Configuration实例
   *
   * @param prefix String
   * @return Configuration List
   */
  List<Configuration> prefixList(String prefix);

  Set<Configuration> prefixSet(String prefix);

  /**
   * 根据prefix获得静态Configuration实例
   *
   * @param prefix String
   * @return Configuration Map
   */
  Map<String, Configuration> prefixMap(String prefix);

  default Optional<String> getString(String key) {
    return get(key);
  }

  default String getString(String key, String defaultValue) {
    return getString(key).orElse(defaultValue);
  }

  default Optional<Integer> getInt(String key) {
    return getValue(key, Integer::parseInt);
  }

  default Integer getInt(String key, Integer defaultValue) {
    return getInt(key).orElse(defaultValue);
  }

  default Optional<Charset> getCharset(String key) {
    return getValue(key, Charset::forName);
  }

  default Charset getCharset(String key, Charset defaultValue) {
    return getCharset(key).orElse(defaultValue);
  }

  default Optional<Long> getLong(String key) {
    return getValue(key, Long::parseLong);
  }

  default Long getLong(String key, Long defaultValue) {
    return getLong(key).orElse(defaultValue);
  }

  default Optional<Float> getFloat(String key) {
    return getValue(key, Float::parseFloat);
  }

  default Float getFloat(String key, Float defaultValue) {
    return getFloat(key).orElse(defaultValue);
  }

  default Optional<Double> getDouble(String key) {
    return getValue(key, Double::parseDouble);
  }

  default Double getDouble(String key, Double defaultValue) {
    return getDouble(key).orElse(defaultValue);
  }

  default Optional<Boolean> getBoolean(String key) {
    return getValue(key, s -> ValueFunctions.toBoolean().parse(s));
  }

  default Boolean getBoolean(String key, Boolean defaultValue) {
    return getBoolean(key).orElse(defaultValue);
  }

  default Optional<BigDecimal> getBigDecimal(String key) {
    return getValue(key, BigDecimal::new);
  }

  default BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
    return getBigDecimal(key).orElse(defaultValue);
  }

  default Optional<BigInteger> getBigInteger(String key) {
    return getValue(key, BigInteger::new);
  }

  default BigInteger getBigInteger(String key, BigInteger defaultValue) {
    return getBigInteger(key).orElse(defaultValue);
  }

  default Optional<File> getFile(String key) {
    return getValue(key, File::new);
  }

  default File getFile(String key, File defaultValue) {
    return getFile(key).orElse(defaultValue);
  }

  default Optional<Path> getPath(String key) {
    return getValue(key, Paths::get);
  }

  default Path getPath(String key, Path defaultValue) {
    return getPath(key).orElse(defaultValue);
  }

  default Optional<Calendar> getCalendar(String key) {
    return getValue(key, s -> ValueFunctions.toCalendar().parse(s));
  }

  default Calendar getCalendar(String key, Calendar defaultValue) {
    return getCalendar(key).orElse(defaultValue);
  }

  default Optional<LocalTime> getLocalTime(String key) {
    return getValue(key, LocalTime::parse);
  }

  default LocalTime getLocalTime(String key, LocalTime defaultValue) {
    return getLocalTime(key).orElse(defaultValue);
  }

  default Optional<LocalDateTime> getLocalDateTime(String key) {
    return getValue(key, LocalDateTime::parse);
  }

  default LocalDateTime getLocalDateTime(String key, LocalDateTime defaultValue) {
    return getLocalDateTime(key).orElse(defaultValue);
  }

  default Optional<LocalDate> getLocalDate(String key) {
    return getValue(key, LocalDate::parse);
  }

  default LocalDate getLocalDate(String key, LocalDate defaultValue) {
    return getLocalDate(key).orElse(defaultValue);
  }

  default Optional<URI> getURI(String key) {
    return getValue(key,

        s -> ValueFunctions.toURI().parse(s));
  }

  default URI getURI(String key, URI defaultValue) {
    return getURI(key).orElse(defaultValue);
  }


  default Optional<URL> getURL(String key) {
    return getValue(key,
        s -> ValueFunctions.toURL().parse(s)
    );
  }

  default URL getURL(String key, URL defaultValue) {
    return getURL(key).orElse(defaultValue);
  }

  default <T extends Enum<T>> Optional<T> getEnum(String key, Class<T> enumType) {
    return getValue(key, s -> Enum.valueOf(enumType, s));
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
    return get(key).map(
        s -> ValueFunctions.toArray(separator).parse(s));
  }

  default String[] getArray(String key, String separator, String[] defaultValue) {
    return getArray(key, separator).orElse(defaultValue);
  }

  default List<String> getList(String key, String separator, List<String> defaultValue) {
    return get(key).map(s -> ValueFunctions.toList(separator).parse(s)).orElse(defaultValue);
  }

  default Optional<List<String>> getList(String key, String separator) {
    return get(key).map(
        s -> ValueFunctions.toList(separator).parse(s));
  }

  default Optional<List<String>> getList(String key) {
    return get(key).map(
        s -> ValueFunctions.toList(",").parse(s));
  }

  default List<String> getList(String key, List<String> defaultValue) {
    return get(key).map(s -> ValueFunctions.toList(",").parse(s)).orElse(defaultValue);
  }

  default <T> List<T> getList(String key, String separator, Parser<T> map) {
    return getList(key, separator).orElse(Collections.emptyList()).stream().map(map::parse)
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  default <T> T[] getArray(String key, String separator, Parser<T> map) {
    return (T[]) stream(getArray(key, separator).orElse(new String[0]))
        .map(map::parse).toArray();
  }

  default <T> List<T> getList(String key, Parser<T> map) {
    return getList(key, ",").orElse(Collections.emptyList()).stream().map(map::parse)
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  default <T> T[] getArray(String key, Parser<T> map) {
    return (T[]) stream(getArray(key, ",").orElse(new String[0]))
        .map(map::parse).toArray();
  }
}
