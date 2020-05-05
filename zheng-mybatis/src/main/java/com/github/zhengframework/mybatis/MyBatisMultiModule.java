package com.github.zhengframework.mybatis;

/*-
 * #%L
 * zheng-mybatis
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

import static com.google.inject.name.Names.named;

import com.github.zhengframework.configuration.ConfigurationAwareModule;
import com.github.zhengframework.configuration.ConfigurationBeanMapper;
import java.util.Map;
import java.util.Map.Entry;

public class MyBatisMultiModule extends ConfigurationAwareModule {

  @Override
  protected void configure() {
    Map<String, MyBatisConfig> myBatisConfigMap =
        ConfigurationBeanMapper.resolve(getConfiguration(), MyBatisConfig.class);
    for (Entry<String, MyBatisConfig> entry : myBatisConfigMap.entrySet()) {
      String name = entry.getKey();
      MyBatisConfig myBatisConfig = entry.getValue();
      if (!name.isEmpty()) {
        install(new MyBatisPrivateModule(named(name), myBatisConfig));
      } else {
        install(new MyBatisPrivateModule(null, myBatisConfig));
      }
    }
  }
}
