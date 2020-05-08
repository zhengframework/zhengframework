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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class HomeDirectoryLocationStrategy implements FileLocationStrategy {

  private static final String PROP_HOME = "user.home";
  private final String homeDirectory;
  private boolean withBasePath;

  public HomeDirectoryLocationStrategy(String homeDir, final boolean withBasePath) {
    this.homeDirectory = fetchHomeDirectory(homeDir);
    this.withBasePath = withBasePath;
  }

  public HomeDirectoryLocationStrategy(boolean withBasePath) {
    this(null, withBasePath);
  }

  public HomeDirectoryLocationStrategy() {
    this(null, false);
  }

  private static String fetchHomeDirectory(final String homeDir) {
    return homeDir != null ? homeDir : System.getProperty(PROP_HOME);
  }

  @Override
  public Optional<URL> locate(FileSystem fileSystem, FileLocator locator) {
    if (StringUtils.isNotEmpty(locator.getFileName())) {
      final String basePath = fetchBasePath(locator);
      final File file = new File(basePath, locator.getFileName());
      if (file.isFile()) {
        try {
          log.debug("Loading configuration from the file ({})", file.getCanonicalPath());
          return Optional.of(file.toURI().toURL());
        } catch (IOException e) {
          return Optional.empty();
        }
      }
    }
    return Optional.empty();
  }

  private String fetchBasePath(final FileLocator locator) {
    if (withBasePath && StringUtils.isNotEmpty(locator.getBasePath())) {

      final StringBuilder fName = new StringBuilder();
      fName.append(homeDirectory);
      if (!homeDirectory.endsWith(File.separator)) {
        fName.append(File.separator);
      }
      String ext = locator.getBasePath();
      if (ext.startsWith("." + File.separator)) {
        fName.append(ext.substring(2));
      } else {
        fName.append(ext);
      }
      return fName.toString();
    }
    return homeDirectory;
  }
}
