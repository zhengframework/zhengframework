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

import java.lang.reflect.Method;

@SuppressWarnings("rawtypes")
class Utils {

  public static boolean isPackageValid(final Class type) {
    boolean res = false;
    if (type != null) {
      final Package pack = type.getPackage();
      res = !(pack == null || pack.getName().startsWith("java"));
    }
    return res;
  }

  public static void checkNoParams(final Method method) {
    if (method.getParameterTypes().length > 0) {
      throw new IllegalStateException("Method without parameters required");
    }
  }
}
