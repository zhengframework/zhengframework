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

import com.github.zhengframework.configuration.ex.ConfigurationSourceException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.tomlj.Toml;
import org.tomlj.TomlParseError;
import org.tomlj.TomlParseResult;

public class TomlConfigurationParser implements ConfigurationParser, FileConfigurationParser {

  @Override
  public Map<String, String> parse(InputStream inputStream) {
    try {
      Map<String, String> map = new HashMap<>();
      TomlParseResult tomlParseResult = Toml.parse(inputStream);
      if (tomlParseResult.hasErrors()) {
        for (TomlParseError error : tomlParseResult.errors()) {
          throw error;
        }
      }
      for (String s : tomlParseResult.dottedKeySet(false)) {
        Object o = tomlParseResult.get(s);
        map.put(s, String.valueOf(o));
      }
      return map;
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from inputStream", e);
    }
  }

  @Override
  public String[] supportFileTypes() {
    return new String[]{".properties"};
  }

  @Override
  public Map<String, String> parse(String fileName, InputStream inputStream) {
    checkSupportFileTypes(fileName);
    try {
      Map<String, String> map = new HashMap<>();
      TomlParseResult tomlParseResult = Toml.parse(inputStream);
      if (tomlParseResult.hasErrors()) {
        for (TomlParseError error : tomlParseResult.errors()) {
          throw error;
        }
      }
      for (String s : tomlParseResult.dottedKeySet(false)) {
        Object o = tomlParseResult.get(s);
        map.put(s, String.valueOf(o));
      }
      return map;
    } catch (IOException e) {
      throw new ConfigurationSourceException("fail load configuration from file: " + fileName, e);
    }
  }
}
