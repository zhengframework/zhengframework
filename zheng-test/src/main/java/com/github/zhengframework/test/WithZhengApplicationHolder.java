package com.github.zhengframework.test;

/*-
 * #%L
 * zheng-test
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

import com.github.zhengframework.configuration.Configuration;
import com.github.zhengframework.core.ModuleProvider;
import com.github.zhengframework.test.WithZhengApplication.NotDefineConfiguration;
import com.google.inject.Module;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@ToString
@NoArgsConstructor
@Setter(AccessLevel.PACKAGE)
@Getter(AccessLevel.PACKAGE)
@SuppressWarnings("unchecked")
class WithZhengApplicationHolder {

  private String configFile;
  private Class<? extends Configuration> configurationClass;
  private String[] arguments = new String[0];
  private Class<? extends Module>[] moduleClass = new Class[0];
  private Class<? extends Module>[] excludeModuleClass = new Class[0];
  private Class<? extends ModuleProvider>[] excludeModuleProviderClass = new Class[0];
  private boolean autoLoadModule = true;

  public WithZhengApplicationHolder(WithZhengApplication withZhengApplication) {
    configFile = StringUtils.trimToNull(withZhengApplication.configFile());
    if (!withZhengApplication.configurationClass().equals(NotDefineConfiguration.class)) {
      configurationClass = withZhengApplication.configurationClass();
    }
    arguments = withZhengApplication.arguments();
    moduleClass = withZhengApplication.moduleClass();
    excludeModuleClass = withZhengApplication.excludeModuleClass();
    excludeModuleProviderClass = withZhengApplication.excludeModuleProviderClass();
    autoLoadModule = withZhengApplication.autoLoadModule();
  }

  public static WithZhengApplicationHolder create(WithZhengApplication withZhengApplication) {
    return new WithZhengApplicationHolder(withZhengApplication);
  }

  public WithZhengApplicationHolder merge(WithZhengApplicationHolder merged) {
    WithZhengApplicationHolder holder = new WithZhengApplicationHolder();
    holder.setArguments(getArguments());
    holder.setConfigFile(getConfigFile());
    holder.setConfigurationClass(getConfigurationClass());
    holder.setAutoLoadModule(isAutoLoadModule());
    holder.setExcludeModuleClass(getExcludeModuleClass());
    holder.setExcludeModuleProviderClass(getExcludeModuleProviderClass());
    holder.setModuleClass(getModuleClass());

    if (merged.getConfigFile() != null) {
      holder.setConfigFile(merged.getConfigFile());
    }
    if (merged.getConfigurationClass() != null) {
      holder.setConfigurationClass(merged.getConfigurationClass());
    }
    holder.setExcludeModuleClass(
        ArrayUtils.addAll(holder.getExcludeModuleClass(), merged.getExcludeModuleClass()));
    holder.setExcludeModuleProviderClass(
        ArrayUtils.addAll(
            holder.getExcludeModuleProviderClass(), merged.getExcludeModuleProviderClass()));
    holder.setModuleClass(ArrayUtils.addAll(holder.getModuleClass(), merged.getModuleClass()));
    holder.setAutoLoadModule(merged.isAutoLoadModule());
    holder.setArguments(ArrayUtils.addAll(holder.getArguments(), merged.getArguments()));
    return holder;
  }

  public WithZhengApplicationHolder merge(WithZhengApplication mergedAnnotation) {
    WithZhengApplicationHolder merged = new WithZhengApplicationHolder(mergedAnnotation);
    return merge(merged);
  }
}
