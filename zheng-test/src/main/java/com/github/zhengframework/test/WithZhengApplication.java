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
import com.google.inject.Module;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Inherited
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WithZhengApplication {

  String configFile() default "";

  Class<? extends Configuration> configurationClass() default NotDefineConfiguration.class;

  String[] arguments() default {};

  Class<? extends Module>[] moduleClass() default {};

  Class<? extends Module>[] excludeModuleClass() default {};

  Class<? extends ModuleProvider>[] excludeModuleProviderClass() default {};

  boolean autoLoadModule() default true;

  class NotDefineConfiguration implements Configuration {

    @Override
    public Optional<String> get(String key) {
      return Optional.empty();
    }

    @Override
    public Set<String> keySet() {
      return null;
    }

    @Override
    public Map<String, String> asMap() {
      return null;
    }

    @Override
    public Configuration prefix(String prefix) {
      return null;
    }

    @Override
    public List<Configuration> prefixList(String prefix) {
      return null;
    }

    @Override
    public Set<Configuration> prefixSet(String prefix) {
      return null;
    }

    @Override
    public Map<String, Configuration> prefixMap(String prefix) {
      return null;
    }
  }
}
