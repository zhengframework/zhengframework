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
import java.io.InputStream;
import java.net.URL;

public abstract class FileSystem {

  private volatile FileOptionsProvider optionsProvider;

  public FileOptionsProvider getFileOptionsProvider() {
    return this.optionsProvider;
  }

  public void setFileOptionsProvider(final FileOptionsProvider provider) {
    this.optionsProvider = provider;
  }

  public abstract InputStream getInputStream(URL url) throws ConfigurationException;

  public abstract URL locateFromURL(String basePath, String fileName);
}
