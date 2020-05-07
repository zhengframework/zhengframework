package com.github.zhengframework.configuration.io;

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

import com.github.zhengframework.configuration.ex.ConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

@Slf4j
public class DefaultFileSystem extends FileSystem {

  @Override
  public InputStream getInputStream(URL url) throws ConfigurationException {
    final File file = FileUtils.toFile(url);
    if (file != null && file.isDirectory()) {
      throw new ConfigurationException("Cannot load a configuration from a directory");
    }
    try {
      return url.openStream();
    } catch (final Exception e) {
      throw new ConfigurationException("Unable to load the configuration from the URL " + url, e);
    }
  }

  @Override
  public URL locateFromURL(String basePath, String fileName) {
    try {
      URL url;
      if (basePath == null) {
        return new URL(fileName);
      }
      final URL baseURL = new URL(basePath);
      url = new URL(baseURL, fileName);

      try (InputStream in = url.openStream()) {}
      return url;
    } catch (IOException e) {
      log.debug("Could not locate file {} at {}: {}", fileName, basePath, e.getMessage());
      return null;
    }
  }
}
