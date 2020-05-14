package com.github.zhengframework.jpa;

/*-
 * #%L
 * zheng-jpa
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

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import com.github.zhengframework.guice.ExposedPrivateModule;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JpaModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    JpaConfig jpaConfig =
        ConfigurationBeanMapper.resolve(getConfiguration(), JpaConfig.ZHENG_JPA, JpaConfig.class);
    if (jpaConfig.getPersistenceUnitName() == null) {
      jpaConfig.setPersistenceUnitName("zheng-jpa");
    }
    install(new JpaInternalModule(jpaConfig));

    Class<? extends ExposedPrivateModule> extraModuleClass = jpaConfig.getExtraModuleClass();
    if (extraModuleClass != null) {
      try {
        ExposedPrivateModule module = extraModuleClass.getDeclaredConstructor().newInstance();
        log.info("install an extra module: " + extraModuleClass.getName());
        install(module);
      } catch (InstantiationException
          | IllegalAccessException
          | InvocationTargetException
          | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
