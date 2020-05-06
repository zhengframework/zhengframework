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

import com.github.zhengframework.configuration.parser.Parser;
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
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class ValueFunctions {

  private static final Set<String> TRUE = ImmutableSet.of("true", "yes", "on", "1");
  private static final Set<String> FALSE = ImmutableSet.of("false", "no", "off", "0");
  private static final Parser<Boolean> STRING_TO_BOOLEAN =
      value -> {
        String lower = Strings.nullToEmpty(value).toLowerCase(Locale.ENGLISH);
        if (TRUE.contains(lower)) {
          return true;
        } else if (FALSE.contains(lower)) {
          return false;
        }
        throw new IllegalStateException("cannot parse boolean value: " + value);
      };

  public static Parser<Boolean> toBoolean() {
    return STRING_TO_BOOLEAN;
  }

  public static Parser<Calendar> toCalendar() {
    return s -> {
      Calendar c = Calendar.getInstance(TimeZone.getDefault(),Locale.ENGLISH);
      try {
        c.setTime(new Date(Long.parseLong(s)));
        return c;
      } catch (NumberFormatException ignored) {
      }
      try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("",Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getDefault());
        c.setTime(dateFormat.parse(s));
      } catch (ParseException e) {
        throw new RuntimeException("illegal value", e);
      }
      return c;
    };
  }

  public static Parser<URI> toURI() {
    return s -> {
      try {
        return new URI(s);
      } catch (URISyntaxException e) {
        throw new RuntimeException("illegal value", e);
      }
    };
  }

  public static Parser<URL> toURL() {
    return s -> {
      try {
        return new URL(s);
      } catch (MalformedURLException e) {
        throw new RuntimeException("illegal value", e);
      }
    };
  }

  public static Parser<String[]> toArray(String separator) {
    return value -> value.split(separator);
  }

  public static Parser<List<String>> toList(String separator) {
    return value -> Arrays.asList(value.split(separator));
  }
}
