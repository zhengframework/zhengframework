// package com.github.zhengframework.configuration.parser;

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
//
// import java.lang.reflect.ParameterizedType;
// import java.lang.reflect.Type;
//
// public abstract class GenericType<T> {
//
//  private final Type type;
//
//  public GenericType() {
//    if (GenericType.class != getClass().getSuperclass()) {
//      String errorMsg = "'%s' must be a direct subclass of '%s'";
//      errorMsg = String.format(errorMsg, getClass().getName(), GenericType.class.getName());
//      throw new IllegalArgumentException(errorMsg);
//    }
//    Type t = getClass().getGenericSuperclass();
//    if (t instanceof ParameterizedType) {
//      ParameterizedType superClass = (ParameterizedType) t;
//      type = superClass.getActualTypeArguments()[0];
//    } else {
//      String message = "'%s' must be parameterized (for example \"new
// GenericType<List<Integer>>(){}\"), "
//          + "it cannot be of raw type \"new GenericType(){}\".";
//      throw new IllegalStateException(String.format(message, getClass().getName()));
//    }
//  }
//
//  public final Type getType() {
//    return type;
//  }
//
//  @Override
//  final public String toString() {
//    return type.toString();
//  }
//
// }
