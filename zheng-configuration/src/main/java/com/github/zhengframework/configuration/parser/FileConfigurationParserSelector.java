package com.github.zhengframework.configuration.parser;

/*-
 * #%L
 * zheng-configuration
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

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.lang3.ArrayUtils;

public class FileConfigurationParserSelector implements FileConfigurationParser {

  private final List<FileConfigurationParser> parsers;
  private final String[] supportFileTypes;

  public FileConfigurationParserSelector(List<FileConfigurationParser> parsers) {
    this.parsers = Objects.requireNonNull(parsers);

    supportFileTypes =
        parsers.stream()
            .map(FileConfigurationParser::supportFileTypes)
            .flatMap(Arrays::stream)
            .distinct()
            .toArray(String[]::new);
  }

  @Override
  public String[] supportFileTypes() {
    return Arrays.copyOf(supportFileTypes, supportFileTypes.length);
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    String s = Objects.requireNonNull(fileName);
    String type = s.substring(s.lastIndexOf("."));
    Optional<FileConfigurationParser> first =
        parsers.stream().filter(p -> ArrayUtils.contains(p.supportFileTypes(), type)).findFirst();
    if (first.isPresent()) {
      return first.get().parse(fileName, inputStream);
    }
    throw new IllegalStateException("not find any FileConfigurationParser for type: " + type);
  }
}
