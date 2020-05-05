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

import com.github.zhengframework.configuration.ex.UnresolvablePlaceholdersException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class copy from project 'owner'
 */
public class PlaceHolder {

  private static final Pattern PATTERN = Pattern.compile("\\$\\{(.+?)}");
  private final Configuration configuration;

  /**
   * Creates a new instance and initializes it. Uses defaults for variable prefix and suffix and the
   * escaping character.
   *
   * @param configuration the variables' values, may be null
   */
  public PlaceHolder(Configuration configuration) {
    this.configuration = configuration;
  }

  /**
   * Replaces all the occurrences of variables with their matching values from the resolver using
   * the given source string as a template.
   *
   * @param source the string to replace in, null returns null
   * @return the result of the replace operation
   */
  public String replace(String source) {
    if (source == null) {
      return null;
    }
    Matcher m = PATTERN.matcher(source);
    StringBuffer sb = new StringBuffer();
    while (m.find()) {
      String var = m.group(1);
      Optional<String> value = configuration.get(var);
      if (!value.isPresent()) {
        throw new UnresolvablePlaceholdersException(var);
      }
      String replacement = replace(value.get());
      m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
    }
    m.appendTail(sb);
    return sb.toString();
  }

  /**
   * Returns a string modified in according to supplied source and arguments.
   *
   * If the source string has pattern-replacement content like {@code "a.${var}.b"}, the pattern is
   * replaced property value of "var".
   *
   * Otherwise the return string is formatted by source and arguments as with {@link
   * String#format(String, Object...)}
   *
   * @param source A source formatting format string. {@code null} returns {@code null}
   * @param args Arguments referenced by the format specifiers in the source string.
   * @return formatted string
   */
  public String replace(String source, Object... args) {
    if (source == null) {
      return null;
    }
    Matcher m = PATTERN.matcher(source);
    return m.find() ? replace(source) : String.format(source, args);
  }

}
