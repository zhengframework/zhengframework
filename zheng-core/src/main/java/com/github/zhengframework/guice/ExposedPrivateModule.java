package com.github.zhengframework.guice;

/*-
 * #%L
 * zheng-core
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

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExposedPrivateModule extends AbstractModule {

  private List<Key> exposeList = new ArrayList<>();

  public List<Key> getExposeList() {
    return exposeList;
  }

  protected void expose(Key key) {
    exposeList.add(key);
  }

  protected void expose(Class clazz) {
    exposeList.add(Key.get(clazz));
  }
}
