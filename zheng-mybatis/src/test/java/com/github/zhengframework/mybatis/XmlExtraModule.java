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

import com.github.zhengframework.guice.ExposedPrivateModule;
import org.apache.ibatis.jdbc.ScriptRunner;

public class XmlExtraModule extends ExposedPrivateModule {

  @Override
  protected void configure() {
    bind(UserDAO.class).to(UserDAOAImpl.class);
    bind(FooService.class).to(FooServiceMapperAImpl.class);
    bind(ScriptRunner.class).toProvider(ScriptRunnerProvider.class);
    expose(UserDAO.class);
    expose(FooService.class);
    expose(ScriptRunner.class);
  }
}
